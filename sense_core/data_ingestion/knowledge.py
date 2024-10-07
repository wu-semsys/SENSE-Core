import re
from typing import List

from rdflib import URIRef
from data_ingestion.configuration import GraphDbConfiguration
from data_ingestion.data_source import DataSource, InfluxDataSource, PointDescription
from shared.configuration import InfluxDBConfiguration
from shared.exceptions import KnowledgeBaseException
from shared.graphdb_utils import get_string, get_uri, query_multiple


class DataIngestionKnowledgeRepository:
    def __init__(self, graphDbConfig: GraphDbConfiguration, influxDbConfig: InfluxDBConfiguration) -> None:
        self.graphDbConfig = graphDbConfig
        self.influxDbConfig = influxDbConfig

    def find_all_points(self) -> List[DataSource]:
        # Find InfluxDB points
        bindings = query_multiple(
            self.graphDbConfig,
            """
            PREFIX s: <http://w3id.org/explainability/sense#>
            PREFIX sosa: <http://www.w3.org/ns/sosa/>

            select ?point ?timeseries_id where { 
                ?point rdf:type sosa:Sensor .
                ?point s:dataSource ?timeseries_id .
            }
            """,
        )

        results = []
        for binding in bindings:
            uri = get_uri("point", binding)
            timeseries_id = get_string("timeseries_id", binding)
            data_source = create_influxdb_data_source(self.influxDbConfig, uri, timeseries_id)
            results.append(data_source)
        return results


TIMESERIES_ID_REGEX = r"measurement=(\w+),field=(.+)"


def create_influxdb_data_source(config: InfluxDBConfiguration, uri: URIRef, timeseries_id: str) -> PointDescription:
    match = re.match(TIMESERIES_ID_REGEX, timeseries_id)
    if not match:
        raise KnowledgeBaseException(f"Timeseries ID {timeseries_id} does not match expected format.")
    measurement = match.group(1)
    field = match.group(2)
    point_wrapper = PointDescription(uri, measurement, field)
    return InfluxDataSource(config, point_wrapper)
