import datetime
from typing import List
from simple_event_detection.event_broker import EventBroker
from shared.model import (
    DetectedEvent,
    EventDetectionProcedure,
)
from simple_event_detection.monitors import WindowBasedMonitor
from simple_event_detection.monitors.time_series import TimeSeries
from simple_event_detection.monitors.utils import get_required_literal_parameter


class StableTimeMonitor(WindowBasedMonitor):
    """
    This class is able to monitor a stable time event ("stable" signal).

    method: "CustomMonitor"
    definition: "StableTimeMonitor"
    parameters
        - value: binds to the value of the monitored signal.
        - maximum_delta: Largest offset that is still considered stable within a stable_time frame.
        - stable_time: Within which period a difference should be observed [s].
    """

    def __init__(self, procedure: EventDetectionProcedure, event_broker: EventBroker) -> None:
        maximum_delta = get_required_literal_parameter(procedure, "maximum_delta")
        stable_time = get_required_literal_parameter(procedure, "stable_time")

        super().__init__(procedure, event_broker, stable_time)
        self.maximum_delta = maximum_delta
        self.stable_time = stable_time
        self.signal = TimeSeries()
        self.old_value = False

    def evaluate_window(self, window) -> List[DetectedEvent]:
        """
        Checks whether no values in a sliding window (size stable_time) have a difference of more than
        maximum_delta. If this is the case we emit an event.

        Note that currently we do not interpolate the signals or look beyond the window.
        """
        events = []

        if len(window) < 2:
            return []

        has_significant_diff = False
        for j, (_, value1) in enumerate(window):
            for _, value2 in window[j + 1 :]:
                if value2 - value1 >= self.maximum_delta:
                    has_significant_diff = True
                    break
            if has_significant_diff:
                break

        is_stable = not has_significant_diff
        if is_stable and not self.old_value:
            (timestamp_int, _) = window[0]
            timestamp = datetime.datetime.fromtimestamp(timestamp_int, datetime.timezone.utc)
            event = DetectedEvent(self.procedure.sensor_uri, timestamp, is_stable, self.procedure)
            events.append(event)

        self.old_value = is_stable

        return events

    def reset_data(self) -> None:
        super().reset_data()
        self.old_value = False
        self.signal = TimeSeries()
