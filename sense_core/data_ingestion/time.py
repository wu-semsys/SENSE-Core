import datetime


class FakeClock:
    def __init__(self, startAt: datetime.datetime, deltaInSeconds: int) -> None:
        self.current = startAt
        self.deltaInSeconds = deltaInSeconds
    
    def now(self) -> datetime.datetime:
        return self.current

    def tick(self) -> None:
        self.current = self.current + datetime.timedelta(seconds=self.deltaInSeconds)