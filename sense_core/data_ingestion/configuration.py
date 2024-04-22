import datetime
import json
from shared.configuration import GraphDbConfiguration, InfluxDBConfiguration, MqttConfiguration

class DataIngestionTimeConfiguration:
    def __init__(self, config: dict):
        self.startAt = datetime.datetime.fromisoformat(config["startAt"]).replace(tzinfo=datetime.timezone.utc)
        self.deltaInSeconds = int(config["deltaInSeconds"])


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
