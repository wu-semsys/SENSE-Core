package sense.explanationinterface.Config;

public class QueryConfig {
    public final String SENSOR_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:Sensor.\n" +
            "}";

    public final String SENSOR_TYPE_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:SensorType.\n" +
            "}";

    public final String STATE_TYPE_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:StateType.\n" +
            "}";

    public final String TEMPORAL_RELATION_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:temporalRelation.\n" +
            "}";

    public final String TOPOLOGICAL_RELATION_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:topologicalRelation.\n" +
            "}";

    public final String CAUSAL_RELATION_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:causalRelation.\n" +
            "}";

    public final String EVENT_TYPE_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://w3id.org/explainability/sense#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:EventType.\n" +
            "}";

    public final String OBSERVABLE_PROPERTY_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:ObservableProperty.\n" +
            "}";

    public final String PROCEDURE_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:Procedure.\n" +
            "}";

    public final String RESULT_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a sosa:Result.\n" +
            "}";

    public final String STATE_TYPE_CAUSALITY_QUERY = "{new-prefixes}\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
            "PREFIX mod: <https://w3id.org/mod#> \n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX s: <http://example.org/sense-ontology#> \n" +
            "PREFIX ssn: <https://www.w3.org/ns/ssn/> \n" +
            "\n" +
            "insert {\n" +
            "    Graph <namedGraph> { \n" +
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "    ?s a s:StateTypeCausality.\n" +
            "}";

    public final String EVENT_QUERY = "{new-prefixes}\n" +
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
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "   VALUES ?s { <eventURI> }\n" +
            "    ?s a s:Event .\n" +
            "}";

    public final String CHECK_EVENT_TYPE_QUERY =
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

    public final String STATE_QUERY = "{new-prefixes}\n" +
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
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "   VALUES ?s { <stateURI> }\n" +
            "    ?s a s:State .\n" +
            "}";

    public final String OBSERVATION_QUERY = "{new-prefixes}\n" +
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
            "        {new-statements}\n" +
            "    }\n" +
            "}\n" +
            "where {\n" +
            "   VALUES ?s { <observationURI> }\n" +
            "    ?s a sosa:Observation .\n" +
            "}";

    public final String EXPLANATION_SELECT_QUERY = "PREFIX sense:  <http://w3id.org/explainability/sense#>\n" +
            "    PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "    PREFIX : <baseURI>\n" +
            "\n" +
            "    SELECT distinct ?causesensor ?cause ?cset ?ceet ?relation ?effectsensor ?effect ?set ?eet\n" +
            "    WHERE {{\n" +
            "        ?cause sense:causallyRelated ?effect .\n" +
            "        ?effect (sense:causallyRelated)* :StateToExplain .\n" +
            "        <<?cause sense:causallyRelated ?effect>> sense:hasCausalSource ?stc .\n" +
            "        ?stc sense:causalRelation ?relation .\n" +
            "        ?causeobservation sosa:hasResult ?cause .\n" +
            "        ?causesensor sosa:madeObservation ?causeobservation .\n" +
            "        ?effectobservation sosa:hasResult ?effect .\n" +
            "        ?effectsensor sosa:madeObservation ?effectobservation .\n" +
            "        ?effect sense:hasStartEvent ?se .\n" +
            "        ?effect sense:hasEndEvent ?ee .\n" +
            "        ?seo sosa:hasResult ?se .\n" +
            "        ?seo sosa:phenomenonTime ?set .\n" +
            "        ?eeo sosa:hasResult ?ee .\n" +
            "        ?eeo sosa:phenomenonTime ?eet .\n" +
            "        ?cause sense:hasStartEvent ?cse .\n" +
            "        ?cause sense:hasEndEvent ?cee .\n" +
            "        ?cseo sosa:hasResult ?cse .\n" +
            "        ?cseo sosa:phenomenonTime ?cset .\n" +
            "        ?ceeo sosa:hasResult ?cee .\n" +
            "        ?ceeo sosa:phenomenonTime ?ceet .\n" +
            "    }}\n";

    public final String STATE_TO_EXPLAIN = "PREFIX : <baseURI>\n" +
            "    PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "    PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "    SELECT ?v WHERE {{ \n" +
            "        ?v a s:State .\n" +
            "        ?v s:hasStateType ?st .\n" +
            "        ?st s:isTriggerState true .\n" +
            "        ?v s:hasStartEvent ?e .\n" +
            "        ?o sosa:hasResult ?e .\n" +
            "        ?o sosa:phenomenonTime ?t .\n" +
            "        FILTER (?t <= 'datetime_str'^^xsd:dateTime)\n" +
            "    }} ORDER BY DESC(?t)\n" +
            "    LIMIT 1";

    public final String EXPLANATION_SELECT_QUERY_WITH_USER = "PREFIX sense:  <http://w3id.org/explainability/sense#>\n" +
        "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
        "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
        "PREFIX : <http://example.org/seehub#>\n" +
        "\n" +
        "SELECT distinct ?mitigationoptionlabel ?causesensor ?cause ?cset ?ceet ?relation ?effectsensor ?effect ?set ?eet\n" +
        "WHERE {\n" +
        "\t?cause sense:causallyRelated ?effect .\n" +
        "\t?effect (sense:causallyRelated)* :StateToExplain .\n" +
        "\t<<?cause sense:causallyRelated ?effect>> sense:hasCausalSource ?stc .\n" +
        "\t?stc sense:causalRelation ?relation .\n" +
        "\t?causeobservation sosa:hasResult ?cause .\n" +
        "\t?causesensor sosa:madeObservation ?causeobservation .\n" +
        "\t?effectobservation sosa:hasResult ?effect .\n" +
        "\t?effectsensor sosa:madeObservation ?effectobservation .\n" +
        "\t?effect sense:hasStartEvent ?se .\n" +
        "\t?effect sense:hasEndEvent ?ee .\n" +
        "\t?seo sosa:hasResult ?se .\n" +
        "\t?seo sosa:phenomenonTime ?set .\n" +
        "\t?eeo sosa:hasResult ?ee .\n" +
        "\t?eeo sosa:phenomenonTime ?eet .\n" +
        "\t?cause sense:hasStartEvent ?cse .\n" +
        "\t?cause sense:hasEndEvent ?cee .\n" +
        "\t?cseo sosa:hasResult ?cse .\n" +
        "\t?cseo sosa:phenomenonTime ?cset .\n" +
        "\t?ceeo sosa:hasResult ?cee .\n" +
        "\t?ceeo sosa:phenomenonTime ?ceet .\n" +
        "    \n" +
        "    # filter on user view access rights\n" +
        "    ?causeplatform sosa:hosts ?causesensor .\n" +
        "    ?user rdfs:label 'user_role' .\n" +
        "    ?user sense:hasUserRole ?userrole .\n" +
        "    ?userrole sense:containsAccessRights ?accessright .\n" +
        "    ?accessright sense:allowsViewAccessTo ?causeplatform .\n" +
        "    OPTIONAL {\n" +
        "    \t?accessright sense:allowsControlAccessTo ?causeplatform .\n" +
        "        ?cause sense:hasStateType ?causeType .\n" +
        "    \t?causeType sense:hasMitigationPlan ?mitigationplan .\n" +
        "    \t?mitigationplan sense:containsMitigationOption ?mitigationoption .\n" +
        "    \t?mitigationoption rdfs:label ?mitigationoptionlabel .\n" +
        "        }\n" +
        "}";

    public final String STATE_TO_EXPLAIN_WITH_USER =
        "PREFIX : <baseURI>\n" +
            "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "SELECT ?v WHERE {\n" +
            "    ?v a s:State .\n" +
            "    ?v s:hasStateType ?st .\n" +
            "    ?st s:isTriggerState true .\n" +
            "    ?v s:hasStartEvent ?e .\n" +
            "    ?o sosa:hasResult ?e .\n" +
            "    ?o sosa:phenomenonTime ?t .\n" +
            "    \n" +
            "    # Link the observation ?o back to its sensor and then to its hosting platform\n" +
            "    ?causesensor sosa:madeObservation ?o .\n" +
            "    ?causeplatform sosa:hosts ?causesensor .\n" +
            "    \n" +
            "    FILTER (?t <= 'datetime_str'^^xsd:dateTime)\n" +
            "    \n" +
            "    # Apply the user view access filter\n" +
            "    ?user rdfs:label 'user_role' .\n" +
            "    ?user s:hasUserRole ?userrole .\n" +
            "    ?userrole s:containsAccessRights ?accessright .\n" +
            "    ?accessright s:allowsViewAccessTo ?causeplatform .\n" +
            "}\n" +
            "ORDER BY DESC(?t)\n" +
            "LIMIT 1";

    public final String STATE_TO_EXPLAIN_WITH_USER_AND_STATE =
        "PREFIX : <baseURI>\n" +
            "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "SELECT ?v WHERE {\n" +
            "    ?v a s:State .\n" +
            "    ?v s:hasStateType ?st .\n" +
            "    ?st s:isTriggerState true .\n" +
            "    ?st rdfs:label ?stName .\n" +
            "    ?v s:hasStartEvent ?e .\n" +
            "    ?o sosa:hasResult ?e .\n" +
            "    ?o sosa:phenomenonTime ?t .\n" +
            "    \n" +
            "    # Link the observation ?o back to its sensor and then to its hosting platform\n" +
            "    ?causesensor sosa:madeObservation ?o .\n" +
            "    ?causeplatform sosa:hosts ?causesensor .\n" +
            "    \n" +
            "    FILTER (?t <= 'datetime_str'^^xsd:dateTime)\n" +
            "    FILTER CONTAINS(LCASE(?stName), LCASE('state_type'))\n" +
            "    \n" +
            "    # Apply the user view access filter\n" +
            "    ?user rdfs:label 'user_role' .\n" +
            "    ?user s:hasUserRole ?userrole .\n" +
            "    ?userrole s:containsAccessRights ?accessright .\n" +
            "    ?accessright s:allowsViewAccessTo ?causeplatform .\n" +
            "}\n" +
            "ORDER BY DESC(?t)\n" +
            "LIMIT 1";

    public final String STATE_TO_EXPLAIN_WITH_STATE = "PREFIX : <baseURI>\n" +
        "    PREFIX s: <http://w3id.org/explainability/sense#>\n" +
        "    PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
        "    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
        "    SELECT ?v WHERE {{ \n" +
        "        ?v a s:State .\n" +
        "        ?v s:hasStateType ?st .\n" +
        "        ?st s:isTriggerState true .\n" +
        "        ?st rdfs:label ?stName .\n" +
        "        ?v s:hasStartEvent ?e .\n" +
        "        ?o sosa:hasResult ?e .\n" +
        "        ?o sosa:phenomenonTime ?t .\n" +
        "        FILTER (?t <= 'datetime_str'^^xsd:dateTime)\n" +
        "        FILTER CONTAINS(LCASE(?stName), LCASE('state_type'))\n" +
        "    }} ORDER BY DESC(?t)\n" +
        "    LIMIT 1";
}
