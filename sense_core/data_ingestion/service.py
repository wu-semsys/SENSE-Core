import argparse
import logging
import time
from typing import List
from data_ingestion.configuration import DataIngestionConfiguration, load_configuration
from data_ingestion.data_source import DataSource
from data_ingestion.event_broker import create_mqtt_event_broker
from data_ingestion.knowledge import DataIngestionKnowledgeRepository
from data_ingestion.time import FakeClock
from shared.configuration import GraphDbConfiguration, InfluxDBConfiguration
from shared.graphdb_utils import wait_for_graphdb
from shared.model import Event, SensorEvent
from shared.mqtt_utils import wait_for_mqtt_broker


def find_all_points(config: GraphDbConfiguration, influxDbConfig: InfluxDBConfiguration) -> List[DataSource]:
    knowledge_repository = DataIngestionKnowledgeRepository(config, influxDbConfig)
    return knowledge_repository.find_all_points()


def run_service(config: DataIngestionConfiguration) -> None:
    logging.basicConfig(level=logging.INFO)
    logging.info("Data Ingestion starting...")

    arg_parser = argparse.ArgumentParser(prog="data_ingestion.py")
    arg_parser.add_argument("-c", "--config", required=True)
    args = arg_parser.parse_args()

    config = load_configuration(args.config)

    wait_for_mqtt_broker(config.mqtt)
    wait_for_graphdb(config.semantic_model)

    logging.info("Running Data Ingestion...")
    data_sources = find_all_points(config.semantic_model, config.influxDB)
    logging.info(f"Importing values from {len(data_sources)} data sources...")

    event_broker = create_mqtt_event_broker(config.mqtt)
    clock = FakeClock(config.time.startAt, config.time.deltaInSeconds)
    last_import = clock.now()
    clock.tick()

    logging.info(f"Starting importing ...")
    while True:
        now = clock.now()

        logging.info(f"Importing in range {last_import} - {now}")

        for data_source in data_sources:
            point_values = list(data_source.get_points(last_import, now))
            logging.debug(f"Found {len(point_values)} values for {data_source.uri()}")
            for timestamp, value in point_values:
                event_broker.publish(SensorEvent(data_source.uri(), timestamp, value))

        last_import = now
        clock.tick()
        time.sleep(1)
