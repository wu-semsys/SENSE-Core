package sense.explanationinterface.Config;

public class QueryConfig {
    public final String SENSOR_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/YJUYKjgmPSiM>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:Sensor.\n" +
            "}";

    public final String SENSOR_TYPE_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/UKzAFkIGkuXB>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:SensorType.\n" +
            "}";

    public final String STATE_TYPE_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/LScdlIRrMPMa>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:StateType.\n" +
            "}";

    public final String TEMPORAL_RELATION_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/AllCycnUdgtU>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:temporalRelation.\n" +
            "}";

    public final String TOPOLOGICAL_RELATION_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/oOxXihHNLoar>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:topologicalRelation.\n" +
            "}";

    public final String CAUSAL_RELATION_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/ExOSZFzjOzTs>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:causalRelation.\n" +
            "}";

    public final String EVENT_TYPE_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/wTQLAJxNkfXT>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:EventType.\n" +
            "}";

    public final String OBSERVABLE_PROPERTY_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/hLXtIgDhvFdy>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:ObservableProperty.\n" +
            "}";

    public final String PROCEDURE_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/KGmFypZHKNnj>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:Procedure.\n" +
            "}";

    public final String RESULT_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/RWvKNSJPYZPp>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:Result.\n" +
            "}";

    public final String STATE_TYPE_CAUSALITY_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://example.org/sense-ontology#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?s  ds:compliesWith <https://semantify.it/ds/prJYXYZnZgnZ>;\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:StateTypeCausality.\n" +
            "}";

    public final String EVENT_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/> \n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?event ds:compliesWith <https://semantify.it/ds/jlJCSqhfmLWm>; \n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "   VALUES ?event { <eventURI> }\n" +
            "    ?event a s:Event .\n" +
            "}";

    public final String CHECK_EVENT_TYPE_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/> \n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "\n" +
            "SELECT ?state ?observation ?eventType\n" +
            "WHERE {\n" +
            "    ?state a s:State .\n" +
            "    BIND (<eventURI> AS ?event) .\n" +
            "    { \n" +
            "      \t?state s:hasStartEvent ?event .\n" +
            "      \tBIND(\"startEvent\" AS ?eventType) .\n" +
            "      \t?observation a sosa:Observation .\n" +
            "      \t?observation sosa:hasResult ?event .\n" +
            "    }\n" +
            "    UNION\n" +
            "    { \n" +
            "      \t?state s:hasEndEvent ?event .\n" +
            "      \tBIND(\"endEvent\" AS ?eventType) .\n" +
            "      \t?observation a sosa:Observation .\n" +
            "      \t?observation sosa:hasResult ?event .\n" +
            "    }\n" +
            "}\n";

    public final String CHECK_EVENT_TYPE_WITHOUT_STATE_QUERY = "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "select ?observation ?eventType where { \n" +
            "\tBIND (<eventURI> AS ?event) .\n" +
            "    ?observation sosa:hasResult ?event .\n" +
            "    ?event s:hasEventType ?eventType .\n" +
            "}\n";

    public final String STATE_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/> \n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?state ds:compliesWith <https://semantify.it/ds/otyMdbLoTejZ>; \n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "   VALUES ?state { <stateURI> }\n" +
            "    ?state a s:State .\n" +
            "}";

    public final String OBSERVATION_QUERY = "PREFIX ds: <https://vocab.sti2.at/ds/> \n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        ?observation ds:compliesWith <https://semantify.it/ds/dTpocSrnzomJ>; \n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "   VALUES ?observation { <observationURI> }\n" +
            "    ?observation a sosa:Observation .\n" +
            "}";

    public final String EXPLANATION_SELECT_QUERY = "PREFIX sense:  <http://w3id.org/explainability/sense#>\n" +
            "    PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "    PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "    SELECT distinct ?causesensor ?cause ?cset ?ceet ?relation ?effectsensor ?effect ?set ?eet\n" +
            "    WHERE {{\n" +
            "        ?cause ?relation ?effect .\n" +
            "        ?effect (s:causallyRelated)* s:StateToExplain .\n" +
            "        ?relation rdfs:subPropertyOf s:causallyRelated .\n" +
            "        ?causeobservation sosa:hasResult ?cause .\n" +
            "        ?causesensor sosa:madeObservation ?causeobservation .\n" +
            "        ?effectobservation sosa:hasResult ?effect .\n" +
            "        ?effectsensor sosa:madeObservation ?effectobservation .\n" +
            "        ?effect s:hasStartEvent ?se .\n" +
            "        ?effect s:hasEndEvent ?ee .\n" +
            "        ?seo sosa:hasResult ?se .\n" +
            "        ?seo sosa:phenomenonTime ?set .\n" +
            "        ?eeo sosa:hasResult ?ee .\n" +
            "        ?eeo sosa:phenomenonTime ?eet .\n" +
            "        ?cause s:hasStartEvent ?cse .\n" +
            "        ?cause s:hasEndEvent ?cee .\n" +
            "        ?cseo sosa:hasResult ?cse .\n" +
            "        ?cseo sosa:phenomenonTime ?cset .\n" +
            "        ?ceeo sosa:hasResult ?cee .\n" +
            "        ?ceeo sosa:phenomenonTime ?ceet .\n" +
            "        FILTER(?relation != s:causallyRelated) .\n" +
            "    }}";

    public final String STATE_TO_EXPLAIN = "PREFIX : <http://example.org/seehub#>\n" +
            "    PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "    PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "    SELECT ?v WHERE {{ \n" +
            "        ?v a s:State .\n" +
            "        ?v s:hasStateType :DemandEnvelopeViolation_State .\n" +
            "        ?v s:hasStartEvent ?e .\n" +
            "        ?o sosa:hasResult ?e .\n" +
            "        ?o sosa:phenomenonTime ?t .\n" +
            "        FILTER (?t <= 'datetime_str'^^xsd:dateTime)\n" +
            "    }} ORDER BY DESC(?t)\n" +
            "    LIMIT 1";
}
