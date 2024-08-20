import json


class MqttConfiguration:
    def __init__(self, config: dict) -> None:
        self.host = config["host"]
        self.port = config["port"]
        self.semantic_event_log_id = config.get("semantic-event-log-id")
        self.simple_event_detection_id = config.get("simple-event-detection-id")
        self.data_ingestion_id = config.get("data-ingestion-id")
        self.event_to_state_causality_id = config.get("event-to-state-causality-id")

    def __str__(self) -> str:
        return (f"Host: {self.host}, Port: {self.port}, Semantic Event Log ID: {self.semantic_event_log_id}, "
                f"Simple Event Detection ID: {self.simple_event_detection_id}, Data Ingestion ID: {self.data_ingestion_id}, "
                f"Event to State Causality ID: {self.event_to_state_causality_id}")


class GraphDbConfiguration:
    def __init__(self, config: dict) -> None:
        self.host = config["host"]
        self.port = config["port"]
        self.repository = config["repository"]
        self.named_graph = config.get("named-graph")

    def __str__(self) -> str:
        return f"Host: {self.host}, Port: {self.port}, Repository: {self.repository}, Named Graph: {self.named_graph}"


class InfluxDBConfiguration:
    def __init__(self, config: dict) -> None:
        self.host = config["host"]
        self.port = config["port"]
        self.protocol = config["protocol"]
        self.token = config["token"]
        self.org = config["org"]
        self.bucket = config["bucket"]

    def __str__(self) -> str:
        return (f"Host: {self.host}, Port: {self.port}, Protocol: {self.protocol}, "
                f"Token: {self.token}, Org: {self.org}, Bucket: {self.bucket}")
