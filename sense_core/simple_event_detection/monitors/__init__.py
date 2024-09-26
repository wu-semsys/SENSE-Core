from shared.model import (
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
