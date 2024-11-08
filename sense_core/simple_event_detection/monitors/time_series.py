from optparse import Option
from typing import Generator, List, Tuple


class TimeSeries:
    def __init__(self) -> None:
        self.values = []

    def add_value(self, timestamp: int, value: float):
        self.values.append((timestamp, value))

    def clean_up(self, min_timestamp: int):
        """
        Removes all items from the time-series that lies beyond the min_timestamp.
        Note that we will keep one entry before the min_timestamp.
        """

        bigger_timestamp_idx = None
        for i in range(0, len(self.values)):
            (timestamp, _) = self.values[i]
            if timestamp > min_timestamp:
                bigger_timestamp_idx = i

        if bigger_timestamp_idx > 0:
            self.values = self.values[(bigger_timestamp_idx - 1) :]
