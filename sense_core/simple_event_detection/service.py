import logging
from simple_event_detection.app_context import create_app_context
from simple_event_detection.configuration import MonitoringConfiguration
from shared.graphdb_utils import wait_for_graphdb
from shared.mqtt_utils import wait_for_mqtt_broker


def run_service(config: MonitoringConfiguration) -> None:
    logging.basicConfig(level=logging.INFO)
    logging.info("Monitoring Service starting...")
    logging.info("Config: " + str(config))

    wait_for_mqtt_broker(config.mqtt)
    wait_for_graphdb(config.graphdb)
    app_context = create_app_context(config)
    app_context.run()

    logging.info("Monitoring Service shutting down...")
