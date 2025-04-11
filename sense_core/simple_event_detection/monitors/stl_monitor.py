import datetime
from typing import List
from simple_event_detection.event_broker import EventBroker
from shared.model import (
    DetectedEvent,
    EventDetectionProcedure,
)
from rtamt import StlDenseTimeSpecification
from simple_event_detection.monitors import Monitor


def create_logic_specification(
    procedure: EventDetectionProcedure,
) -> StlDenseTimeSpecification:
    spec = StlDenseTimeSpecification()
    spec.spec = procedure.definition
    for var in procedure.parameter_bindings:
        spec.declare_var(var.name, "float")
    spec.parse()
    spec.pastify()
    return spec


class STLMonitor(Monitor):
    """
    This class is able to monitor an STL formula over the event stream.

    method: "STL"
    definition: "<STL Formula>" (e.g., eventually[0:10](X > 100))
    parameters: Parameters bind variables in the formula to sensor values. Variables cannot be
                part of time constraints (e.g., [0:10] from above).
    """

    def __init__(self, procedure: EventDetectionProcedure, event_broker: EventBroker) -> None:
        super().__init__(procedure, event_broker)
        self.old_value = False
        self.specification = create_logic_specification(procedure)

    def evaluate_internal(self, updates) -> List[DetectedEvent]:
        events = []

        step_updates = list(updates.items())
        robustness = self.specification.update(*step_updates)
        for t, r in robustness:
            # If the robustness is exactly zero, the specification is between being fulfilled and violated.
            # If this is the case we simply do nothing for this iteration.
            if r != 0:
                boolean_value = r > 0
                if boolean_value == True and self.old_value == False:
                    timestamp = datetime.datetime.fromtimestamp(t, datetime.timezone.utc)
                    event = DetectedEvent(self.procedure.sensor_uri, timestamp, boolean_value, self.procedure)
                    events.append(event)
                self.old_value = boolean_value

        return events

    def reset_data(self) -> None:
        super().reset_data()
        self.old_value = False
        self.specification = create_logic_specification(self.procedure)
