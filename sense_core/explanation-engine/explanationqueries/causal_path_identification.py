import re
from SPARQLWrapper import SPARQLWrapper, BASIC, JSON

def ccToLines(label):
    label = re.sub(r'((?<=[a-z])[A-Z]|(?<!\A)[A-Z](?=[a-z]))', r'\n\1', label)
    return label

def run_select_query(StateToExplain, config):
    query = f"""
    PREFIX sense:  <http://w3id.org/explainability/sense#>
    PREFIX sosa: <http://www.w3.org/ns/sosa/>
    PREFIX : <http://example.org/seehub#>

    SELECT distinct ?causesensor ?cause ?cset ?ceet ?relation ?effectsensor ?effect ?set ?eet
    WHERE {{
        ?cause sense:causallyRelated ?effect .
        ?effect (sense:causallyRelated)* :{StateToExplain} .
        <<?cause sense:causallyRelated ?effect>> sense:hasCausalSource ?stc .
        ?stc sense:causalRelation ?relation .
        ?causeobservation sosa:hasResult ?cause .
        ?causesensor sosa:madeObservation ?causeobservation .
        ?effectobservation sosa:hasResult ?effect .
        ?effectsensor sosa:madeObservation ?effectobservation .
        ?effect sense:hasStartEvent ?se .
        ?effect sense:hasEndEvent ?ee .
        ?seo sosa:hasResult ?se .
        ?seo sosa:phenomenonTime ?set .
        ?eeo sosa:hasResult ?ee .
        ?eeo sosa:phenomenonTime ?eet .
        ?cause sense:hasStartEvent ?cse .
        ?cause sense:hasEndEvent ?cee .
        ?cseo sosa:hasResult ?cse .
        ?cseo sosa:phenomenonTime ?cset .
        ?ceeo sosa:hasResult ?cee .
        ?ceeo sosa:phenomenonTime ?ceet .
    }}
    """

    endpoint = f"http://{config['semantic-model']['host']}:{config['semantic-model']['port']}/repositories/{config['semantic-model']['repository']}"
    sparql = SPARQLWrapper(endpoint)
    sparql.setQuery(query)

    if "user" in config["semantic-model"] and "password" in config["semantic-model"]:
        sparql.setCredentials(config["semantic-model"]["user"], config["semantic-model"]["password"])
        sparql.setHTTPAuth(BASIC)

    sparql.setReturnFormat(JSON)

    try:
        results = sparql.queryAndConvert()
    except Exception as e:
        print(f"An error occurred while querying the SPARQL endpoint: {e}")
        raise

    explanations = []
    for result in results["results"]["bindings"]:
        cause = {
            "value": result["cause"]["value"],
            "sensor": result["causesensor"]["value"],
            "startTime": result["cset"]["value"],
            "endTime": result["ceet"]["value"]
        }
        effect = {
            "value": result["effect"]["value"],
            "sensor": result["effectsensor"]["value"],
            "startTime": result["set"]["value"],
            "endTime": result["eet"]["value"]
        }
        relation = result["relation"]["value"]
        explanations.append({
            "cause": cause,
            "effect": effect,
            "relation": relation
        })

    return explanations

def get_state_to_explain(datetime_str, config):
    query = f"""
    PREFIX : <http://example.org/seehub#>
    PREFIX sense: <http://w3id.org/explainability/sense#>
    PREFIX sosa: <http://www.w3.org/ns/sosa/>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    SELECT ?v WHERE {{ 
        ?v a sense:State .
        ?v sense:hasStateType :DemandEnvelopeViolation_State .
        ?v sense:hasStartEvent ?e .
        ?o sosa:hasResult ?e .
        ?o sosa:phenomenonTime ?t .
        FILTER (?t <= "{datetime_str}"^^xsd:dateTime)
    }} ORDER BY DESC(?t)
    LIMIT 1
    """

    endpoint = f"http://{config['semantic-model']['host']}:{config['semantic-model']['port']}/repositories/{config['semantic-model']['repository']}"
    sparql = SPARQLWrapper(endpoint)
    sparql.setQuery(query)

    if "user" in config["semantic-model"] and "password" in config["semantic-model"]:
        sparql.setCredentials(config["semantic-model"]["user"], config["semantic-model"]["password"])
        sparql.setHTTPAuth(BASIC)

    sparql.setReturnFormat(JSON)

    try:
        results = sparql.queryAndConvert()
    except Exception as e:
        print(f"An error occurred while querying the SPARQL endpoint: {e}")
        raise

    if results["results"]["bindings"]:
        return results["results"]["bindings"][0]["v"]["value"].split("#")[1]
    else:
        return None
