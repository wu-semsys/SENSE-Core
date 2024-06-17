import json


class MqttConfiguration:
    def __init__(self, config: dict) -> None:
        self.host = config["host"]
        self.port = config["port"]
        self.client_id = config["clientId"]

    def __str__(self) -> str:
        return f"Host: {self.host}, Port: {self.port}, Client ID: {self.client_id}"


class GraphDbConfiguration:
    def __init__(self, config: dict) -> None:
        self.host = config["host"]
        self.port = config["port"]
        self.repository = config["repository"]

    def __str__(self) -> str:
        return f"Host: {self.host}, Port: {self.port}, Repository: {self.repository}"


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
