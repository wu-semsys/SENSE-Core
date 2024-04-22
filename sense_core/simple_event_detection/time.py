import datetime
from abc import ABC, abstractmethod
from shared.model import Event


class Clock(ABC):
    @abstractmethod
    def now(self) -> datetime.datetime:
        pass


class EventBasedClock:
    def __init__(self) -> None:
        self.current_timestamp = datetime.datetime.min.replace(tzinfo=datetime.timezone.utc)

    def now(self) -> datetime.datetime:
        return self.current_timestamp

    def on_event(self, event: Event) -> None:
        if self.current_timestamp is None or event.timestamp > self.current_timestamp:
            self.current_timestamp = event.timestamp
