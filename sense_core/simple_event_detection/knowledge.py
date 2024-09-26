from typing import List, Tuple

from rdflib import Namespace, URIRef
from shared.configuration import GraphDbConfiguration
from shared.graphdb_utils import get_literal, get_string, get_uri, query_multiple
from shared.model import (
    EventDetectionProcedure,
    LiteralParameterBinding,
    SignalParameterBinding,
    ParameterBinding,
)

SENSE = Namespace("http://w3id.org/explainability/sense#")


class MonitoringKnowledgeRepository:
    def __init__(self, graph_db_config: GraphDbConfiguration) -> None:
        self.graph_db_config = graph_db_config

    def find_all_event_detection_procedures(self) -> List[EventDetectionProcedure]:
        bindings = query_multiple(
            self.graph_db_config,
            """
            PREFIX s: <http://w3id.org/explainability/sense#>
            PREFIX sosa: <http://www.w3.org/ns/sosa/>
            PREFIX ssn: <http://www.w3.org/ns/ssn/>

            select ?eventDetection ?sensor ?property ?eventType ?method ?definition where { 
                ?eventDetection a s:EventDetection ;
                    s:method ?method ;
                    s:definition ?definition .
                ?sensor ssn:implements ?eventDetection ;
                    sosa:observes ?property .
                ?eventType s:detectedBy ?eventDetection ;
            }
            """,
        )

        results = []
        for binding in bindings:
            event_detection = get_uri("eventDetection", binding)
            sensor = get_uri("sensor", binding)
            property = get_uri("property", binding)
            event = get_uri("eventType", binding)
            method = get_string("method", binding)
            definition = get_string("definition", binding)
            parameter_bindings = self.__resolve_parameter_bindings(event_detection)
            results.append(
                EventDetectionProcedure(
                    event_detection, sensor, property, event, method, definition, parameter_bindings
                )
            )
        return results

    #
    # Helper Methods
    #

    def __resolve_parameter_bindings(self, signal_ref: URIRef) -> List[ParameterBinding]:
        parameter_bindings = []

        signal_bound_variables_result = query_multiple(
            self.graph_db_config,
            f"""
            PREFIX s: <http://w3id.org/explainability/sense#>

            select ?name ?sensor where {{
                    <{signal_ref}> s:hasParameterBinding [
                        a s:ParameterBinding ;
                        s:parameterName ?name ;
                        s:bindsToSensor ?sensor
                    ] .
            }}
            """,
        )
        for result in signal_bound_variables_result:
            name = get_string("name", result)
            signal = get_uri("sensor", result)
            parameter_bindings.append(SignalParameterBinding(name, signal))

        literal_bound_variables_result = query_multiple(
            self.graph_db_config,
            f"""
            PREFIX s: <http://w3id.org/explainability/sense#>

            select ?name ?literal where {{
                    <{signal_ref}> s:hasParameterBinding [
                        a s:ParameterBinding ;
                        s:parameterName ?name ;
                        s:bindsToLiteral ?literal
                    ] .
            }}
            """,
        )
        for result in literal_bound_variables_result:
            name = get_string("name", result)
            literal = get_literal("literal", result)
            parameter_bindings.append(LiteralParameterBinding(name, float(literal)))

        return parameter_bindings
