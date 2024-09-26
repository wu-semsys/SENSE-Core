import json
from shared.configuration import MqttConfiguration, GraphDbConfiguration

class Configuration:
    def __init__(self, config: dict) -> None:
        self.mqtt = MqttConfiguration(config["mqtt"])
        self.event_log = GraphDbConfiguration(config["semantic-model"])
        
    def __str__(self) -> str:
        return (f"MQTT Configuration: {self.mqtt}\n"
                f"GraphDB Configuration: {self.event_log}\n")


def load_configuration(path: str) -> Configuration:
    with open(path) as config_file:
        json_data = json.loads(config_file.read())
        return Configuration(json_data)
