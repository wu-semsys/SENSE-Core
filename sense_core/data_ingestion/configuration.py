import datetime
import json
from shared.configuration import GraphDbConfiguration, InfluxDBConfiguration, MqttConfiguration


class DataIngestionTimeConfiguration:
    def __init__(self, config: dict):
        self.mode = config["mode"]
        self.sleep_in_seconds = int(config["sleepInSeconds"])

        if self.mode not in {"live", "replay"}:
            raise Exception(f"Unknown Mode: {self.mode}")


class LiveDataIngestionTimeConfiguration(DataIngestionTimeConfiguration):
    def __init__(self, config: dict):
        super().__init__(config)


class ReplayDataIngestionTimeConfiguration(DataIngestionTimeConfiguration):
    def __init__(self, config: dict):
        super().__init__(config)
        self.start_at = datetime.datetime.fromisoformat(config["startAt"]).replace(tzinfo=datetime.timezone.utc)
        self.stop_at = datetime.datetime.fromisoformat(config["stopAt"]).replace(tzinfo=datetime.timezone.utc)
        self.delta_in_seconds = int(config["deltaInSeconds"])
        self.stop_action = config["stopAction"]

        if self.stop_action not in {"repeat", "stop"}:
            raise Exception(f"Unknown Stop Action: {self.stop_action}")

def load_time_configuration(json_data: dict) -> DataIngestionTimeConfiguration:
    if json_data["mode"] == "live":
        return LiveDataIngestionTimeConfiguration(json_data)

    if json_data["mode"] == "replay":
        return ReplayDataIngestionTimeConfiguration(json_data)
    
    raise Exception(f"Unknown time mode: {json_data["mode"]}. Supported: {{'live', 'replay'}}")

class DataIngestionConfiguration:
    def __init__(self, config: dict) -> None:
        self.mqtt = MqttConfiguration(config["mqtt"])
        self.influxDB = InfluxDBConfiguration(config["influxdb"])
        self.semantic_model = GraphDbConfiguration(config["semantic-model"])
        self.time = load_time_configuration(config["data-ingestion"])


def load_configuration(path: str) -> DataIngestionConfiguration:
    with open(path) as config_file:
        json_data = json.loads(config_file.read())
        return DataIngestionConfiguration(json_data)
