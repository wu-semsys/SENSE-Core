from abc import ABC
import logging
import uuid
import paho.mqtt.client as mqtt
from typing import Callable
from rdflib import Graph, Literal, Namespace, URIRef
from shared.configuration import MqttConfiguration
from shared.exceptions import EventBrokerException
from shared.model import DetectedEvent, Event, SensorEvent

RDF = Namespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
SOSA = Namespace("http://www.w3.org/ns/sosa/")
SENSE = Namespace("http://w3id.org/explainability/sense#")


class EventBroker(ABC):
    def loop_start(self) -> None:
        pass

    def subscribe(self, signal_uri: str, topic: str, callback: Callable[[SensorEvent], None]) -> None:
        pass

    def publish(self, event: DetectedEvent) -> None:
        pass


class MqttEventBroker:
    def __init__(self, mqtt_configuration: MqttConfiguration) -> None:
        self.mqtt_configuration = mqtt_configuration
        self.clients = []
        self.publish_client = create_mqtt_client(mqtt_configuration)
        self.publish_client.loop_start()

    def loop_start(self) -> None:
        for client in self.clients:
            client.loop_start()

    def subscribe(self, topic: str, callback: Callable[[SensorEvent], None]) -> None:
        def execute_callback(_1, _2, message) -> None:
            payload = Graph().parse(data=message.payload, format="turtle")
            observations = list(payload.triples((None, SOSA["madeObservation"], None)))

            if len(observations) == 0:
                logging.debug("Received message without observation.")
                return

            # TODO: We should do some error handling here.
            for observation in observations:
                sensor_uri = observation[0]
                observation_uri = observation[2]
                result_time = payload.value(observation_uri, SOSA["resultTime"])
                has_result = payload.value(observation_uri, SOSA["hasSimpleResult"])
                callback(SensorEvent(sensor_uri, result_time.toPython(), has_result.toPython()))

        logging.info(f"Subscribing to topic: {topic}")
        client = create_mqtt_client(self.mqtt_configuration)
        client.on_message = execute_callback
        result, _ = client.subscribe(topic=[(topic, 0)])
        if result != mqtt.MQTT_ERR_SUCCESS:
            raise EventBrokerException("Could not subscribe to topic.")
        self.clients.append(client)

    def subscribe_to_system_event(self, callback: Callable[[str], None]) -> None:
        def execute_callback(_1, _2, message) -> None:
            callback(message.payload.decode())

        client = create_mqtt_client(self.mqtt_configuration)
        client.on_message = execute_callback
        result, _ = client.subscribe(topic=[("events/system", 0)])
        if result != mqtt.MQTT_ERR_SUCCESS:
            raise EventBrokerException("Could not subscribe to topic.")
        self.clients.append(client)

    def publish(self, event: DetectedEvent) -> None:
        """
        Publishes a new event on the event broker.
        """

        event_id = uuid.uuid4()
        observation_uri = URIRef(event.source + f"_observation-{event_id}")
        event_uri = URIRef(event.procedure.event_type_uri + f"_{event_id}")
        graph = Graph()
        graph.bind("sense", SENSE)
        graph.add((observation_uri, RDF["type"], SOSA["Observation"]))
        graph.add((URIRef(event.source), SOSA["madeObservation"], observation_uri))
        graph.add((observation_uri, SOSA["phenomenonTime"], Literal(event.timestamp)))
        graph.add((observation_uri, SOSA["usedProcedure"], event.procedure.uri))
        graph.add((observation_uri, SOSA["observedProperty"], event.procedure.property_uri))
        graph.add((observation_uri, SOSA["hasResult"], event_uri))
        graph.add((event_uri, RDF["type"], SENSE["Event"]))
        graph.add((event_uri, SENSE["hasEventType"], event.procedure.event_type_uri))

        logging.info(f"Event Detected: {str(event_uri)}")

        payload = graph.serialize(format="turtle")
        self.publish_client.publish("events/simple", payload)


def create_mqtt_client(configuration: MqttConfiguration) -> mqtt.Client:
    logging.info("Connecting to MQTT broker...")
    client_id = configuration.client_id + "_" + str(uuid.uuid4())
    client = mqtt.Client(client_id=client_id)
    client.connect(configuration.host, configuration.port)

    # Wait for the client to connect. This is due to the fact that the
    # connect method returns after the TCP connection is established, but
    # the MQTT connection is not yet established. We have to loop so that
    # the client can consume the CONNACK message from the broker.
    # https://github.com/eclipse/paho.mqtt.python/issues/454
    while not client.is_connected():
        client.loop(timeout=1)

    logging.info("MQTT broker connection established.")
    return client
