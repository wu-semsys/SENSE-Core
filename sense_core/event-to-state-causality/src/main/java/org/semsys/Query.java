package org.semsys;

public class Query {
    String ADD_NEW_START_STATE = "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "\n" +
            "INSERT {\n" +
            "    GRAPH <http://w3id.org/explainability/sense/States> {\n" +
            "        ?eventSensor sosa:madeObservation sosa:startedStateType_observation_eventURI .\n" +
            "        sosa:startedStateType_observation_eventURI sosa:hasResult s:startedStateType_eventURI ; # define st\n" +
            "        sosa:usedProcedure s:EventToStateConversion ;\n" +
            "        sosa:observedProperty ?observedProperty .\n" +
            "        s:startedStateType_eventURI a s:State ;\n" +
            "                    s:hasStateType ?startedStateType ;\n" +
            "                    s:hasStartEvent <eventURI2> .\n" +
            "    }\n" +
            "}\n" +
            "WHERE {\n" +
            "    VALUES ?event { <eventURI2> } # create state out of event\n" +
            "    # collect event data\n" +
            "    ?event a s:Event ;\n" +
            "           s:hasEventType ?eventType .\n" +
            "    ?eventObservation sosa:hasResult ?event ;\n" +
            "                      sosa:phenomenonTime ?eventTime ;\n" +
            "                      sosa:observedProperty ?observedProperty .\n" +
            "    ?eventSensor sosa:madeObservation ?eventObservation .\n" +
            "    #\n" +
            "    ?startedStateType s:hasStartEventType ?eventType .\n" +
            "}";
    String ADD_NEW_END_STATE = "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "\n" +
            "INSERT {\n" +
            "    GRAPH <http://w3id.org/explainability/sense/States> {\n" +
            "        ?endedState s:hasEndEvent ?event .\n" +
            "    }\n" +
            "}\n" +
            "WHERE {\n" +
            "    VALUES ?event { <eventURI> } # create state out of event\n" +
            "    # collect event data\n" +
            "    ?event a s:Event ;\n" +
            "           s:hasEventType ?eventType .\n" +
            "    ?eventObservation sosa:hasResult ?event ;\n" +
            "                      sosa:phenomenonTime ?eventTime .\n" +
            "    ?eventSensor sosa:madeObservation ?eventObservation .\n" +
            "    ?endedStateType s:hasEndEventType ?eventType .\n" +
            "    ?endedState s:hasStateType ?endedStateType .\n" +
            "    FILTER NOT EXISTS { ?endedState s:hasEndEvent ?endEvent . }\n" +
            "}\n";
    String COLLECT_EVENT_DATA = "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "\n" +
            "SELECT ?event ?eventSensor ?startedStateType ?observedProperty\n" +
            "WHERE {\n" +
            "    VALUES ?event { <eventURI> } # create state out of event\n" +
            "    # collect event data\n" +
            "    ?event a s:Event ;\n" +
            "           s:hasEventType ?eventType .\n" +
            "    ?eventObservation sosa:hasResult ?event ;\n" +
            "                      sosa:phenomenonTime ?eventTime ;\n" +
            "                      sosa:observedProperty ?observedProperty .\n" +
            "    ?eventSensor sosa:madeObservation ?eventObservation .\n" +
            "    #\n" +
            "    ?startedStateType s:hasStartEventType ?eventType .\n" +
            "}\n";

    String ADD_CAUSALITY = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX sense: <http://example.org/sense-ontology#>\n" +
            "PREFIX s: <http://w3id.org/explainability/sense#State>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "insert{\n" +
            "        ?causeState sense:causallyRelated ?effectState .\n" +
            "    \tsense:causallyRelated rdf:label ?causalityType .\n" +
            "    } \n" +
            "where {\n" +
            "    # effect State\n" +
            "    ?effectState a s:State .\n" +
            "    ?effectState s:hasStateType ?effectStateType .\n" +
            "    ?effectObservation sense:observedState ?effectState .\n" +
            "    ?effectObservation sense:startTime ?effectStart .\n" +
            "    ?effectObservation sense:endTime ?effectEnd .\n" +
            "    ?effectSensor sosa:madeObservation ?effectObservation .\n" +
            "    ?effectPlatform sosa:hosts ?effectSensor .\n" +
            "    ?effectParentPlatform sosa:hosts ?effectPlatform .\n" +
            "    # cause State\n" +
            "    ?causeState a sense:State .\n" +
            "    ?causeState sense:hasStateType ?causeStateType .\n" +
            "    ?causeObservation sense:observedState ?causeState .\n" +
            "    ?causeObservation sense:startTime ?causeStart .\n" +
            "    ?causeObservation sense:endTime ?causeEnd .\n" +
            "    ?causeSensor sosa:madeObservation ?causeObservation .\n" +
            "    ?causePlatform sosa:hosts ?causeSensor .\n" +
            "    ?causeParentPlatform sosa:hosts ?causePlatform .\n" +
            "    {\n" +
            "\tselect ?causeStateType ?effectStateType ?causalityType ?platformRequirement ?temporalRelation\n" +
            "\t\twhere { \n" +
            "        ?stc a sense:StateTypeCausality .\n" +
            "        ?stc sense:cause ?causeStateType .    \n" +
            "        ?stc sense:effect ?effectStateType .\n" +
            "        ?stc sense:causalityType ?causalityType .\n" +
            "        ?stc sense:temporalRelation ?temporalRelation .\n" +
            "        ?stc sense:platformRequirement ?platformRequirement .\n" +
            "    \t}\n" +
            "\t}\n" +
            "    BIND(\n" +
            "    \tIF(?platformRequirement = \"samePlatform\", ?causePlatform = ?effectPlatform, \n" +
            "    \tIF(?platformRequirement = \"parentPlatform\", ?causeParentPlatform = ?effectPlatform, \n" +
            "    \tIF(?platformRequirement = \"siblingPlatform\", ?causeParentPlatform = ?effectParentPlatform, true))) as ?Platform_filter\n" +
            "    )\n" +
            "    BIND(\n" +
            "    \tIF(?temporalRelation= \"overlaps\", ?causeStart <= ?effectStart && ?effectStart <= ?causeEnd, \n" +
            "    \tIF(?temporalRelation = \"before\", ?causeStart <= ?effectStart, true)) as ?temporal_filter\n" +
            "    )\n" +
            "  \tFILTER(?Platform_filter && ?temporal_filter)\n" +
            "} ";
}
