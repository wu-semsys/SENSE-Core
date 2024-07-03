import argparse
import datetime
import logging
import time
from typing import List
from data_ingestion.configuration import (
    DataIngestionConfiguration,
    DataIngestionTimeConfiguration,
    LiveDataIngestionTimeConfiguration,
    load_configuration,
)
from data_ingestion.data_source import DataSource
from data_ingestion.event_broker import create_mqtt_event_broker
from data_ingestion.knowledge import DataIngestionKnowledgeRepository
from data_ingestion.time import Clock, FakeClock, RealClock
from shared.configuration import GraphDbConfiguration, InfluxDBConfiguration
from shared.graphdb_utils import wait_for_graphdb
from shared.model import Event, SensorEvent
from shared.mqtt_utils import wait_for_mqtt_broker


def find_all_points(config: GraphDbConfiguration, influxDbConfig: InfluxDBConfiguration) -> List[DataSource]:
    knowledge_repository = DataIngestionKnowledgeRepository(config, influxDbConfig)
    return knowledge_repository.find_all_points()


def should_stop(time_config: DataIngestionTimeConfiguration, now: datetime.datetime) -> bool:
    if isinstance(time_config, LiveDataIngestionTimeConfiguration):
        return False
    return time_config.stop_at < now and time_config.stop_action == "stop"


def should_repeat(time_config: DataIngestionTimeConfiguration, now: datetime.datetime) -> bool:
    if isinstance(time_config, LiveDataIngestionTimeConfiguration):
        return False
    return time_config.stop_at < now and time_config.stop_action == "repeat"


def create_clock(time_config: DataIngestionTimeConfiguration) -> Clock:
    if isinstance(time_config, LiveDataIngestionTimeConfiguration):
        return RealClock()
    return FakeClock(time_config.start_at, time_config.delta_in_seconds)


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
    clock = create_clock(config.time)
    last_import = clock.now()

    time.sleep(config.time.sleep_in_seconds)
    clock.tick()

    logging.info(f"Starting importing ...")
    while True:
        now = clock.now()

        if should_stop(config.time, now):
            break

        elif should_repeat(config.time, now):
            clock = create_clock(config.time)
            logging.info("Restarting scenario. Sending system message...")
            event_broker.publish_system_message("clear_scenario_data")
            # Sleep for some time to wait for the other services to react
            time.sleep(60)
            logging.info(f"Restart complete. Starting from {clock.now()}")
            last_import = clock.now()
            clock.tick()

        else:
            logging.info(f"Importing in range {last_import} - {now}")
            for data_source in data_sources:
                point_values = list(data_source.get_points(last_import, now))
                logging.debug(f"Found {len(point_values)} values for {data_source.uri()}")
                for timestamp, value in point_values:
                    event_broker.publish(SensorEvent(data_source.uri(), timestamp, value))
            last_import = now

            time.sleep(config.time.sleep_in_seconds)
            clock.tick()

    logging.info("Shutting down Data Ingestion ...")
