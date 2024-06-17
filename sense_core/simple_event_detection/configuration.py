import json
from shared.configuration import MqttConfiguration, GraphDbConfiguration


class MonitoringConfiguration:
    def __init__(self, config: dict) -> None:
        self.mqtt = MqttConfiguration(config["mqtt"])
        self.graphdb = GraphDbConfiguration(config["semantic-model"])

    def __str__(self) -> str:
        return (f"MQTT Configuration: {self.mqtt}\n"
                f"GraphDB Configuration: {self.graphdb}\n")

def load_configuration(path: str) -> MonitoringConfiguration:
    with open(path) as config_file:
        json_data = json.loads(config_file.read())
        return MonitoringConfiguration(json_data)

