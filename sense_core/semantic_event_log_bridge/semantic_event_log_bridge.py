import argparse
import logging
import paho.mqtt.client as mqtt
from rdflib import Graph, Namespace
import requests
from configuration import GraphDbConfiguration, MqttConfiguration, load_configuration
from tenacity import retry, stop_after_attempt, wait_fixed
import json

SOSA = Namespace("http://www.w3.org/ns/sosa/")

def extract_has_result(graph: Graph):
    has_result_values = []
    
    for subject, predicate, obj in graph.triples((None, SOSA.hasResult, None)):
        has_result_values.append(obj)
    
    return has_result_values

@retry(wait=wait_fixed(2), stop=stop_after_attempt(10))
def connect_to_mqtt_broker(config: MqttConfiguration) -> mqtt.Client:
    client = mqtt.Client(config.client_id)
    client.connect(config.host, config.port)
    return client


def delete_events_graph(config: GraphDbConfiguration):
    logging.debug(f"Deleting 'Events' graph...")
    params = {"graph": config.event_graph}
    response = requests.delete(
        f"http://{config.host}:{config.port}/repositories/{config.repository}/rdf-graphs/service", params=params
    )
    if response.status_code != 204:
        logging.error(f"Could not delete 'Events' graph in GraphDB. Status code: {response.status_code}")


def insert_graph(config: GraphDbConfiguration, graph: Graph):
    logging.debug(f"Inserting graph into GraphDB...")
    params = {"graph": config.event_graph}
    response = requests.post(
        f"http://{config.host}:{config.port}/repositories/{config.repository}/rdf-graphs/service",
        params=params,
        data=graph.serialize(format="turtle"),
        headers={"Content-Type": "text/turtle"},
    )
    if response.status_code != 204:
        logging.error(f"Could not insert graph into GraphDB. Status code: {response.status_code}")


if __name__ == "__main__":
    arg_parser = argparse.ArgumentParser(prog="rule_based_event_detection.py")
    arg_parser.add_argument("-c", "--config", required=True)
    args = arg_parser.parse_args()
    config = load_configuration(args.config)

    logging.basicConfig(level=logging.DEBUG)
    logging.info("Semantic Event Log Bridge starting...")
    logging.info("config: ")
    with open(args.config) as config_file:
        json_data = json.loads(config_file.read())
        logging.info(json.dumps(json_data))

    # connect to the MQTT broker
    mqtt_client = connect_to_mqtt_broker(config.mqtt)

    # set message callback
    def send_data_to_event_log(client, userdata, msg):

        if msg.topic == "events/system":
            logging.debug(f"System event received: {msg.payload}")
            if msg.payload.decode() == "clear_scenario_data":
                logging.warning("Clearing scenario data by deleting named graph ...")
                delete_events_graph(config.event_log)

        else:
            logging.debug("Sending data to event log...")
            try:
                new_dynamic_graph = Graph().parse(data=msg.payload, format="turtle")
                insert_graph(config.event_log, new_dynamic_graph)
                has_result_values = extract_has_result(new_dynamic_graph)
                for value in has_result_values:
                    logging.debug(f"sosa:hasResult value: {value}")                        
                    result = mqtt_client.publish("events/state", value)
                    logging.debug(f"Sending {value} to events/state")
                    if result.rc != mqtt.MQTT_ERR_SUCCESS:
                        logging.error(f"Failed to publish message: {mqtt.error_string(result.rc)}")
                    else:
                        logging.debug("Message published successfully")
            except Exception as e:
                logging.error("Could not handle event. This may cause an incomplete semantic event log.", e)

    mqtt_client.on_message = send_data_to_event_log

    # subscribe to the events/sensors topic
    logging.info("Subscribing to MQTT topics")
    mqtt_client.subscribe("events/simple")
    mqtt_client.subscribe("events/complex")
    mqtt_client.subscribe("events/system")
    mqtt_client.loop_forever()

    logging.info("Monitoring Service shutting down...")
