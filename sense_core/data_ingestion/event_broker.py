from abc import ABC
import paho.mqtt.client as mqtt
from rdflib import Graph, Literal, Namespace, URIRef
from shared.configuration import MqttConfiguration
from shared.model import SensorEvent
import uuid;

RDF = Namespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
SOSA = Namespace("http://www.w3.org/ns/sosa/")
SENSE = Namespace("http://w3id.org/explainability/sense#")


class EventBroker(ABC):
    def publish(self, event: SensorEvent) -> None:
        pass


class MqttEventBroker:
    def __init__(self, client: mqtt.Client) -> None:
        self.client = client

    def publish(self, event: SensorEvent) -> None:
        """
        Publishes a new event on the event broker.
        """
        observation_uri = URIRef(event.source + f"_observation-{uuid.uuid4()}")
        graph = Graph()
        graph.bind("sense", SENSE)
        graph.add((observation_uri, RDF["type"], SOSA["Observation"]))
        graph.add((event.source, SOSA["madeObservation"], observation_uri))
        graph.add((observation_uri, SOSA["resultTime"], Literal(event.timestamp)))
        graph.add((observation_uri, SOSA["hasSimpleResult"], Literal(event.value)))

        payload = graph.serialize(format="turtle")
        self.client.publish("events/sensors", payload)

    def publish_system_message(self, message: str) -> None:
        """
        Publishes a new system event on the event broker.
        """
        self.client.publish("events/system", message)


def create_mqtt_event_broker(configuration: MqttConfiguration) -> MqttEventBroker:
    client_id = configuration.data_ingestion_id +  "_" + str(uuid.uuid4())
    client = mqtt.Client(client_id=client_id)
    client.connect(configuration.host, configuration.port)

    # Wait for the client to connect. This is due to the fact that the
    # connect method returns after the TCP connection is established, but
    # the MQTT connection is not yet established. We have to loop so that
    # the client can consume the CONNACK message from the broker.
    # https://github.com/eclipse/paho.mqtt.python/issues/454
    while not client.is_connected():
        client.loop(timeout=1)

    return MqttEventBroker(client)
