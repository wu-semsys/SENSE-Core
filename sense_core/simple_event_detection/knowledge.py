from typing import List, Tuple

from rdflib import Namespace, URIRef
from shared.configuration import GraphDbConfiguration
from shared.graphdb_utils import get_string, get_uri, query_multiple
from shared.model import (
    STLEventDetectionProcedure,
    SignalBoundVariable,
    Variable,
)

SENSE = Namespace("http://w3id.org/explainability/sense#")

class MonitoringKnowledgeRepository:
    def __init__(self, graph_db_config: GraphDbConfiguration) -> None:
        self.graph_db_config = graph_db_config

    def find_all_event_detection_procedures(self) -> List[STLEventDetectionProcedure]:
        bindings = query_multiple(
            self.graph_db_config,
            """
            PREFIX s: <http://w3id.org/explainability/sense#>
            PREFIX sosa: <http://www.w3.org/ns/sosa/>
            PREFIX ssn: <https://www.w3.org/ns/ssn/>

            select ?eventDetection ?formula ?sensor ?property ?eventType where { 
                ?eventDetection rdf:type s:EventDetection ;
                    s:detectsEventType ?eventType ;
                    s:hasSTLFormula ?formula .
                ?sensor ssn:implements ?eventDetection ;
                    sosa:observes ?property .
            }
            """,
        )

        results = []
        for binding in bindings:
            event_detection = get_uri("eventDetection", binding)
            formula = get_string("formula", binding)
            sensor = get_uri("sensor", binding)
            property = get_uri("property", binding)
            event = get_uri("eventType", binding)
            variables = self.__resolve_variables(event_detection)
            results.append(STLEventDetectionProcedure(event_detection, sensor, property, event, formula, variables))
        return results

    #
    # Helper Methods
    #

    def __resolve_variables(self, signal_ref: URIRef) -> List[Variable]:
        variables = []

        signal_bound_variables_result = query_multiple(
            self.graph_db_config,
            f"""
            PREFIX s: <http://w3id.org/explainability/sense#>

            select ?name ?signal ?signal_ref ?signal_ref_type where {{
                    <{signal_ref}> s:hasVariable ?var .
                    ?var s:hasName ?name .
                    ?var s:bindsTo ?signal.
            }}
            """,
        )
        for result in signal_bound_variables_result:
            name = get_string("name", result)
            signal = get_uri("signal", result)

            variables.append(SignalBoundVariable(name, signal))

        return variables
