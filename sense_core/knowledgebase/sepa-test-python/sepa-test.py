#!/usr/bin/env python3

# References:
# SEPA engine: https://github.com/vaimee/SEPA
# SEPA python3 library: https://github.com/arces-wot/SEPA-python3-APIs

import os
import time
import yaml
from sepy.SAPObject import SAPObject
from sepy.SEPA import SEPA
from graphdb_utils import query_multiple
from SPARQLWrapper import SPARQLWrapper
from configuration import GraphDbConfiguration

SEPA_TEST_GRAPH = "<http://sense-project.net/sepa-test-graph>"

# Sparql Endpoint Urls
graphdb_config = {
    "host": "localhost",
    "port": 7200,
    "repository": "seehub"
}

def testQueryViaGraphDb():
    print("**running query via graphdb endpoint**")

    config = GraphDbConfiguration(graphdb_config)

    query_string = """
    select * where {
        ?s ?p ?o .
    } limit 10
    """

    print(query_string)

    query_result = query_multiple(config, query_string, False)

    print(query_result)

def testQueryViaSepa(sepa_client):
    print("**running query via sepa query endpoint**")

    query_string = """
    select * where {
        ?s ?p ?o .
    } limit 10
    """

    print(query_string)

    query_result = sepa_client.sparql_query(sparql=query_string)
    print(query_result["results"]["bindings"])


def testSubscribeViaSepa(sepa_client):
    print("**subscribing via sepa query endpoint**")

    query_string = """
    PREFIX sense: <http://w3.org/ns/sense/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    select * where {
        GRAPH <http://sense-project.net/sepa-test-graph> {
            ?s rdf:type sense:EventXXX .
        }
    } limit 10
    """

    try:
        subscription_id = sepa_client.sparql_subscribe(sparql=query_string, alias="Subscriber1", handler=onSemanticEvent)
        print(f"SEPA subscription established via subscription id {subscription_id}")
    except Exception as e:
        print(f"Could not subscribe to GraphDB Endpoint Runtime. ERROR: {e}")

def onSemanticEvent(added, removed):
    print("Received semantic event from SEPA subscription: ")
    print("added: " + str(added))
    print("removed: " + str(removed))

def testInsertMyEventType(sepa_client):
    print("Inserting MyEventType class")

    updateString = """
        PREFIX sense: <http://w3.org/ns/sense/>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        INSERT DATA
        {
            GRAPH <http://sense-project.net/sepa-test-graph> {
                sense:EventXXX rdfs:subclassOf owl:Class
            }
        }
    """

    try:
        result = sepa_client.sparql_update(sparql=updateString)
        print(result)
    except Exception as e:
        print(f"Could not send sparql_update to SEPA Endpoint. ERROR: {e}")

def testInsertViaSepa(sepa_client, event_name):
    print("sending update")

    updateString = """
        PREFIX sense: <http://w3.org/ns/sense/>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        INSERT DATA
        {
            GRAPH <http://sense-project.net/sepa-test-graph> {
                sense:<event_name> rdf:type sense:EventXXX
            }
        }
    """

    updateString = updateString.replace("<event_name>", event_name)

    try:
        print(updateString)
        result = sepa_client.sparql_update(sparql=updateString)
        print(result)
    except Exception as e:
        print(f"Could not send sparql_update to SEPA Endpoint. ERROR: {e}")

def testDeleteIndividualViaSepa(sepa_client, event_name):
    print("deleting existing event")

    updateString = """
        PREFIX sense: <http://w3.org/ns/sense/>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        DELETE DATA
        {
            GRAPH <http://sense-project.net/sepa-test-graph> {
                sense:<event_name> rdf:type sense:EventXXX
            }
        }
    """

    updateString = updateString.replace("<event_name>", event_name)

    try:
        print(updateString)
        result = sepa_client.sparql_update(sparql=updateString)
        print(result)
    except Exception as e:
        print(f"Could not send sparql_update to SEPA Endpoint. ERROR: {e}")

if __name__ == '__main__':
    print("sepa-test")

    # send a test query to the graphdb endpoint (to make sure we did not brake anything by activating SEPA
    testQueryViaGraphDb()

    # Create a SEPA client to be reused for the foolowing tests
    # Create an asolute path to endpoint.yasp
    script_dir = os.path.dirname(os.path.abspath(__file__))
    file_name = 'endpoint.yasp'
    file_path = os.path.join(script_dir, file_name)

    # Try to open and read the sepa endpoint config file
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            mySAP = file.read()
            sap = SAPObject(yaml.safe_load(mySAP))
            sc = SEPA(sapObject=sap)
    except FileNotFoundError:
        print(f"The file {file_name} was not found in {script_dir}")
    except Exception as e:
        print(f"Could not read SEPA configuration file. ERROR {e}")
    
    testQueryViaSepa(sc)

    # delete Event1 and Event2 if they exist already
    testDeleteIndividualViaSepa(sc, "Event1")
    testDeleteIndividualViaSepa(sc, "Event2")

    testInsertMyEventType(sc)

    testInsertViaSepa(sc, "Event1")

    testSubscribeViaSepa(sc)

    time.sleep(1)

    # adding an event should be recognized by SEPA and trigger the subscription callback
    testInsertViaSepa(sc, "Event2")

    # deleting events should also be recognized by SEPA:
    testDeleteIndividualViaSepa(sc, "Event1")

    # sleep for two seconds, such that the active subscription can retrieve and output the new values
    time.sleep(2)