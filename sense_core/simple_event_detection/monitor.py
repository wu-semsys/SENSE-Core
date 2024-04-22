import datetime
from simple_event_detection.event_broker import EventBroker
from shared.model import (
    DetectedEvent,
    SensorEvent,
    SignalBoundVariable,
    STLEventDetectionProcedure,
)
from rtamt import StlDenseTimeSpecification


def create_logic_specification(
    procedure: STLEventDetectionProcedure,
) -> StlDenseTimeSpecification:
    spec = StlDenseTimeSpecification()
    spec.spec = procedure.formula
    for var in procedure.variables:
        spec.declare_var(var.name, "float")
    spec.parse()
    return spec


class STLEventDetector:
    """
    This class is able to monitor an STL formula over the event stream.
    """

    def __init__(self, procedure: STLEventDetectionProcedure, event_broker: EventBroker) -> None:
        self.procedure = procedure
        self.event_broker = event_broker
        self.old_value = False
        # TODO Support multiple bindings to same signal
        self.listening_for = {
            var.signal_uri: var.name for var in procedure.variables if isinstance(var, SignalBoundVariable)
        }
        self.updates = dict()
        self.specification = create_logic_specification(procedure)
        self.all_variables_initialized = False

    def on_new_event(self, event: SensorEvent) -> None:
        if not event.source in self.listening_for:
            return

        var = self.listening_for[event.source]
        if not var in self.updates:
            self.updates[var] = []
        self.updates[var].append((int(event.timestamp.timestamp()), event.value))

    def list_required_signal_bindings(self):
        return [x.signal_binding for x in self.procedure.variables if isinstance(x, SignalBoundVariable)]

    def evaluate(self):
        if not self.all_variables_initialized and len(self.updates) < len(self.listening_for):
            return

        step_updates = list(self.updates.items())
        if len(step_updates) > 0:
            robustness = self.specification.update(*step_updates)
            for t, r in robustness:
                boolean_value = r >= 0
                if boolean_value == True and self.old_value == False:
                    timestamp = datetime.datetime.fromtimestamp(t, datetime.timezone.utc)
                    event = DetectedEvent(self.procedure.sensor_uri, timestamp, boolean_value, self.procedure)
                    self.event_broker.publish(event)
                self.old_value = boolean_value

        self.all_variables_initialized = True
        self.updates = dict()
