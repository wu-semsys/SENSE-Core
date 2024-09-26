from typing import List
from simple_event_detection.event_broker import EventBroker
from shared.model import (
    DetectedEvent,
    EventDetectionProcedure,
)
from simple_event_detection.monitors import Monitor
from simple_event_detection.monitors.time_series import TimeSeries
from simple_event_detection.monitors.utils import get_required_literal_parameter


class FallTimeMonitor(Monitor):
    """
    This class is able to monitor a fall time event (rapidly decreasing signal).

    method: "CustomMonitor"
    definition: "FallTimeMonitor"
    parameters
        - value: binds to the value of the monitored signal.
        - delta: How far the signal must fall, such that an event is triggered.
        - fall_time: What is the maximum period within which a difference of delta must be observed [s].
    """

    def __init__(self, procedure: EventDetectionProcedure, event_broker: EventBroker) -> None:
        super().__init__(procedure, event_broker)
        self.delta = get_required_literal_parameter(procedure, "delta")
        self.fall_time = get_required_literal_parameter(procedure, "fall_time")
        self.signal = TimeSeries()
        self.old_value = False

    def evaluate_internal(self, updates) -> List[DetectedEvent]:
        """
        Checks whether two values in a sliding window (size fall_time) have a difference of more than
        delta. If this is the case we emit an event.

        Note that currently we do not interpolate the signals or look beyond the window.
        """
        events = []

        for timestamp, value in updates["signal"]:
            self.signal.add_value(timestamp, value)

        for i, (start_time, _) in enumerate(self.signal.values):
            window_end_time = start_time + self.fall_time
            window = [(t, v) for t, v in self.signal.values[i:] if start_time <= t < window_end_time]

            if len(window) < 2:
                break

            has_significant_diff = False
            for j, (_, value1) in enumerate(window):
                for _, value2 in window[j + 1 :]:
                    if value1 - value2 >= self.delta:
                        has_significant_diff = True
                        break
                if has_significant_diff:
                    break

            if has_significant_diff and not self.old_value:
                event = DetectedEvent(self.procedure.sensor_uri, timestamp, has_significant_diff, self.procedure)
                events.append(event)

            self.old_value = has_significant_diff

        (last_timestamp, _) = self.signal.values[-1]
        self.signal.clean_up(last_timestamp - self.fall_time)

        return events

    def reset_data(self) -> None:
        super().reset_data()
        self.old_value = False
        self.signal = TimeSeries()
