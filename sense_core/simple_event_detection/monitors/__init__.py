from typing import List, Tuple
from shared.model import (
    DetectedEvent,
    EventDetectionProcedure,
    SensorEvent,
    SignalParameterBinding,
)
from simple_event_detection.event_broker import EventBroker


class Monitor:
    def __init__(self, procedure: EventDetectionProcedure, event_broker: EventBroker) -> None:
        self.procedure = procedure
        self.event_broker = event_broker
        # TODO Support multiple bindings to same signal
        self.listening_for = {
            var.signal_uri: var.name for var in procedure.parameter_bindings if isinstance(var, SignalParameterBinding)
        }
        self.updates = dict()
        self.all_variables_initialized = False

    def on_new_event(self, event: SensorEvent) -> None:
        if not event.source in self.listening_for:
            return

        var = self.listening_for[event.source]
        if not var in self.updates:
            self.updates[var] = []
        self.updates[var].append((int(event.timestamp.timestamp()), event.value))

    def evaluate(self):
        if not self.all_variables_initialized and len(self.updates) < len(self.listening_for):
            return

        if len(self.updates) == 0:
            return

        events = self.evaluate_internal(self.updates)
        for event in events:
            self.event_broker.publish(event)

        self.all_variables_initialized = True
        self.updates = dict()

    def evaluate_internal(self) -> bool:
        raise NotImplementedError()

    def reset_data(self) -> None:
        self.updates = dict()


class WindowBasedMonitor(Monitor):
    """
    Base class for monitors that work on windows.
    """

    def __init__(self, procedure: EventDetectionProcedure, event_broker: EventBroker, window_size: int, window_complete: bool) -> None:
        super().__init__(procedure, event_broker)
        self.window_size = window_size
        self.window_complete = window_complete

    def evaluate_internal(self, updates) -> List[DetectedEvent]:
        events = []

        for timestamp, value in updates["signal"]:
            self.signal.add_value(timestamp, value)

        (last_timestamp, _) = self.signal.values[-1]
        for i, (start_time, _) in enumerate(self.signal.values):
            window_end_time = start_time + self.window_size

            # If a complete window is requested, there must be enough data.
            if not self.window_complete or last_timestamp >= window_end_time:
                window = [(t, v) for t, v in self.signal.values[i:] if start_time <= t <= window_end_time]
                events.extend(self.evaluate_window(window))

        self.signal.clean_up(last_timestamp - self.window_size)

        return events

    def evaluate_window(self, window: List[Tuple[int, float]]):
        raise NotImplementedError()
