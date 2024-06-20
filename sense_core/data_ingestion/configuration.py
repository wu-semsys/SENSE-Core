import datetime
import json
from shared.configuration import GraphDbConfiguration, InfluxDBConfiguration, MqttConfiguration

class DataIngestionTimeConfiguration:
    def __init__(self, config: dict):
        self.start_at = datetime.datetime.fromisoformat(config["startAt"]).replace(tzinfo=datetime.timezone.utc)
        self.delta_in_seconds = int(config["deltaInSeconds"])
        self.sleep_in_seconds = int(config["sleepInSeconds"])
        self.stop_at = datetime.datetime.fromisoformat(config["stopAt"]).replace(tzinfo=datetime.timezone.utc)
        self.stop_action = config["stopAction"]

        if self.stop_action not in {"repeat", "stop"}:
            raise Exception(f"Unknown Stop Action: {self.stop_action}")


class DataIngestionConfiguration:
    def __init__(self, config: dict) -> None:
        self.mqtt = MqttConfiguration(config["mqtt"])
        self.influxDB = InfluxDBConfiguration(config["influxdb"])
        self.semantic_model = GraphDbConfiguration(config["semantic-model"])
        self.time = DataIngestionTimeConfiguration(config["time"])


def load_configuration(path: str) -> DataIngestionConfiguration:
    with open(path) as config_file:
        json_data = json.loads(config_file.read())
        return DataIngestionConfiguration(json_data)
