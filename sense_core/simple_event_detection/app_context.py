import logging
import time
from typing import List
from simple_event_detection.configuration import MonitoringConfiguration
from simple_event_detection.knowledge import MonitoringKnowledgeRepository
from simple_event_detection.monitors.fall_time_monitor import FallTimeMonitor
from simple_event_detection.monitors.rise_time_monitor import RiseTimeMonitor
from simple_event_detection.monitors.stable_time_monitor import StableTimeMonitor
from simple_event_detection.time import Clock, EventBasedClock
from simple_event_detection.monitors.stl_monitor import STLMonitor
from simple_event_detection.event_broker import MqttEventBroker
from shared.model import EventDetectionProcedure, SensorEvent


def create_custom_monitor(sp: EventDetectionProcedure, broker: MqttEventBroker):
    if sp.definition == "FallTimeMonitor":
        return FallTimeMonitor(sp, broker)

    if sp.definition == "RiseTimeMonitor":
        return RiseTimeMonitor(sp, broker)

    if sp.definition == "StableTimeMonitor":
        return StableTimeMonitor(sp, broker)

    raise Exception("Unknown custom monitor ", sp.definition)


def create_monitor(sp: EventDetectionProcedure, broker: MqttEventBroker):
    if sp.method == "STL":
        return STLMonitor(sp, broker)

    if sp.method == "CustomMonitor":
        return create_custom_monitor(sp, broker)

    raise Exception("Cannot create monitor for method ", sp.method)


class AppContext:
    """
    The AppContext is the central class of the monitoring service. It contains all the
    dependencies and is responsible for initializing the service and starting the
    monitoring process.
    """

    def __init__(
        self,
        clock: Clock,
        event_broker: MqttEventBroker,
        repository: MonitoringKnowledgeRepository,
    ) -> None:
        self.clock = clock
        self.event_broker = event_broker
        self.repository = repository
        self.monitors: List[STLMonitor] = []

    def initialize(self) -> None:
        logging.info("Initializing app context...")
        event_detection_procedures = self.repository.find_all_event_detection_procedures()
        logging.info(f"Found {len(event_detection_procedures)} event detection procedures. Initializing monitors...")
        self.monitors = []
        self.relevant_topics = set()
        for sp in event_detection_procedures:
            monitor = create_monitor(sp, self.event_broker)
            self.monitors.append(monitor)
        logging.info("Created Monitors.")

    def evaluate_monitors(self) -> None:
        for monitor in self.monitors:
            monitor.evaluate()

    def on_new_event(self, event: SensorEvent) -> None:
        for monitor in self.monitors:
            monitor.on_new_event(event)
        self.clock.on_event(event)

    def on_new_system_event(self, event: str) -> None:
        if event == "clear_scenario_data":
            logging.info("Clearing event monitor states ...")
            for monitor in self.monitors:
                monitor.reset_data()

    def run(self) -> None:
        logging.info("Starting monitoring...")
        self.event_broker.subscribe("events/sensors", self.on_new_event)
        self.event_broker.subscribe_to_system_event(self.on_new_system_event)
        self.event_broker.loop_start()
        while True:
            self.evaluate_monitors()  # Recalculate monitored signals
            time.sleep(2)


def create_app_context(config: MonitoringConfiguration) -> AppContext:
    clock = EventBasedClock()
    graphdb_repository = MonitoringKnowledgeRepository(config.graphdb)

    event_broker = MqttEventBroker(config.mqtt)
    app_context = AppContext(clock, event_broker, graphdb_repository)
    app_context.initialize()
    return app_context
