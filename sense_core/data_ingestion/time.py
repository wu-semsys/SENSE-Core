import datetime


class Clock:
    def now(self) -> datetime.datetime:
        pass

    def tick(self) -> None:
        pass


class RealClock(Clock):
    def now(self) -> datetime.datetime:
        return datetime.datetime.now().replace(tzinfo=datetime.timezone.utc)


class FakeClock(Clock):
    def __init__(self, startAt: datetime.datetime, deltaInSeconds: int) -> None:
        self.current = startAt
        self.deltaInSeconds = deltaInSeconds

    def now(self) -> datetime.datetime:
        return self.current

    def tick(self) -> None:
        self.current = self.current + datetime.timedelta(seconds=self.deltaInSeconds)
