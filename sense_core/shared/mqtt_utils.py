import logging
from tenacity import retry, stop_after_attempt, wait_fixed
import paho.mqtt.client as mqtt
from shared.configuration import MqttConfiguration
from shared.exceptions import EventBrokerException


@retry(wait=wait_fixed(2), stop=stop_after_attempt(10))
def try_connecting_to_mqtt_broker(config: MqttConfiguration) -> bool:
    try:
        mqtt_client = mqtt.Client()
        mqtt_client.connect(config.host, config.port)
        mqtt_client.disconnect()
    except Exception:
        logging.warning("MQTT broker is not running.")
        raise EventBrokerException("MQTT broker is not running.")


def wait_for_mqtt_broker(config: MqttConfiguration) -> None:
    logging.info("Waiting for MQTT broker to come online...")
    try_connecting_to_mqtt_broker(config)
    logging.info("MQTT broker is running.")
