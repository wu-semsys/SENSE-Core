from dataclasses import dataclass
import datetime
from typing import Any, List

from rdflib import URIRef


@dataclass
class Variable:
    name: str


@dataclass
class UnboundVariable(Variable):
    pass


@dataclass
class SignalBoundVariable(Variable):
    signal_uri: URIRef


@dataclass
class EventDetectionProcedure:
    uri: URIRef
    sensor_uri: URIRef
    property_uri: URIRef
    event_type_uri: URIRef


@dataclass
class STLEventDetectionProcedure(EventDetectionProcedure):
    formula: str
    variables: List[Variable]


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
