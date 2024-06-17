import json
import logging
from rdflib import URIRef
import requests
from tenacity import retry, stop_after_attempt, wait_fixed
from shared.configuration import GraphDbConfiguration
from shared.exceptions import EventBrokerException, KnowledgeBaseException


def get_uri(variable: str, binding: dict) -> URIRef:
    if not variable in binding:
        raise KnowledgeBaseException(f"Expected to find a binding for variable {variable}.")
    variableBinding = binding[variable]
    if variableBinding["type"] != "uri":
        raise KnowledgeBaseException(f"Variable {variable} did not bind to an URI. This is unexpected behavior.")
    return URIRef(variableBinding["value"])


def get_string(variable: str, binding: dict) -> str:
    if not variable in binding:
        raise KnowledgeBaseException(f"Expected to find a binding for variable {variable}.")

    variableBinding = binding[variable]
    if variableBinding["type"] != "literal":
        raise KnowledgeBaseException(f"Variable {variable} did not bind to a Literal. This is unexpected behavior.")

    value = variableBinding["value"]
    if not isinstance(value, str):
        raise KnowledgeBaseException(f"Expected to find a string literal. Found: {value}.")

    return value


def query_single(config: GraphDbConfiguration, query: str, infer: bool = True):
    result = query_multiple(config, query, infer=infer)
    if len(result) != 1:
        raise KnowledgeBaseException(f"Expected a single result from query. Actual: {len(result)}")
    return result[0]


def query_multiple(config: GraphDbConfiguration, query: str, infer: bool = True):
    headers = {"Accept": "application/sparql-results+json"}
    data = {"query": query, "infer": infer, "sameAs": True}

    url = f"http://{config.host}:{config.port}/repositories/{config.repository}"
    response = requests.post(url, headers=headers, data=data)

    if response.status_code != 200:
        raise KnowledgeBaseException(f"Cannot access knowledge base. Status code: {response.status_code}")

    return json.loads(response.text)["results"]["bindings"]


def wait_for_graphdb(config: GraphDbConfiguration) -> None:
    logging.info("Waiting for GraphDB to come online...")
    try_connecting_to_graphdb(config)
    logging.info("GraphDB is running.")


@retry(wait=wait_fixed(2), stop=stop_after_attempt(30))
def try_connecting_to_graphdb(config: GraphDbConfiguration) -> bool:
    # Make an HTTP request to graphdb to see if it is running.
    try:
        requests.get(f"http://{config.host}:{config.port}/")
    except Exception:
        logging.warning("GraphDB is not running.")
        raise EventBrokerException("GraphDB is not running.")
