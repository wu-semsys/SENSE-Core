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


class RiseTimeMonitor(WindowBasedMonitor):
    """
    This class is able to monitor a rise time event ("rapidly" increasing signal).

    method: "CustomMonitor"
    definition: "RiseTimeMonitor"
    parameters
        - value: binds to the value of the monitored signal.
        - delta: How far the signal must rise, such that an event is triggered.
        - rise_time: What is the maximum period within which a difference of delta must be observed [s].
    """

    def __init__(self, procedure: EventDetectionProcedure, event_broker: EventBroker) -> None:
        delta = get_required_literal_parameter(procedure, "delta")
        rise_time = get_required_literal_parameter(procedure, "rise_time")

        super().__init__(procedure, event_broker, rise_time)
        self.delta = delta
        self.rise_time = rise_time
        self.signal = TimeSeries()
        self.old_value = False

    def evaluate_window(self, window) -> List[DetectedEvent]:
        """
        Checks whether two values in a sliding window (size rise_time) have a difference of more than
        delta. If this is the case we emit an event.

        Note that currently we do not interpolate the signals or look beyond the window.
        """
        events = []

        if len(window) < 2:
            return []

        has_significant_diff = False
        for j, (_, value1) in enumerate(window):
            for _, value2 in window[j + 1 :]:
                if value2 - value1 >= self.delta:
                    has_significant_diff = True
                    break
            if has_significant_diff:
                break

        if has_significant_diff and not self.old_value:
            (timestamp_int, _) = window[0]
            timestamp = datetime.datetime.fromtimestamp(timestamp_int, datetime.timezone.utc)
            event = DetectedEvent(self.procedure.sensor_uri, timestamp, has_significant_diff, self.procedure)
            events.append(event)

        self.old_value = has_significant_diff

        return events

    def reset_data(self) -> None:
        super().reset_data()
        self.old_value = False
        self.signal = TimeSeries()
