from dataclasses import dataclass
import datetime
from typing import Any, List

from rdflib import URIRef


@dataclass
class ParameterBinding:
    name: str


@dataclass
class LiteralParameterBinding(ParameterBinding):
    literal: any


@dataclass
class SignalParameterBinding(ParameterBinding):
    signal_uri: URIRef


@dataclass
class EventDetectionProcedure:
    uri: URIRef
    sensor_uri: URIRef
    property_uri: URIRef
    event_type_uri: URIRef
    method: str
    definition: str
    parameter_bindings: List[ParameterBinding]


@dataclass
class Event:
    source: URIRef
    timestamp: datetime.datetime


@dataclass
class SensorEvent(Event):
    value: Any


@dataclass
class DetectedEvent(Event):
    value: bool
    procedure: EventDetectionProcedure
