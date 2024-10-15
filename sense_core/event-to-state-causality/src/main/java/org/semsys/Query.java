package org.semsys;

public class Query {
    String ADD_NEW_START_STATE = "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "\n" +
            "INSERT {\n" +
            "    GRAPH <namedGraphURI> {\n" +
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
            "    GRAPH <namedGraphURI> {\n" +
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

    String ADD_CAUSALITY = "PREFIX sense: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
            "PREFIX s: <http://w3id.org/explainability/sense#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "insert{\n" +
            "    GRAPH <namedGraphURI> {\n" +
            "        ?causeState s:causallyRelated ?effectState . \n" +
            "        << ?causeState s:causallyRelated ?effectState >> sense:hasCausalSource ?stc .
"
            "       \n" +
            "   }\n" +
            "}\n" +
            "WHERE {\n" +
            "    # effect State\n" +
            "    ?effectState a sense:State .\n" +
            "    ?effectState sense:hasStateType ?effectStateType .\n" +
            "    ?effectObservation sosa:hasResult ?effectState .\n" +
            "    ?effectState s:hasEndEvent ?endEvent .\n" +
            "    ?effectState s:hasStartEvent ?startEvent .\n" +
            "    ?effectSensor sosa:madeObservation ?effectObservation .\n" +
            "    ?effectPlatform sosa:hosts ?effectSensor .\n" +
            "    ?effectParentPlatform sosa:hosts ?effectPlatform .\n" +
            "    \n" +
            "    OPTIONAL {\n" +
            "        ?effectObservation1 sosa:hasResult ?endEvent .\n" +
            "        ?endEvent a s:Event .\n" +
            "        ?effectObservation1 sosa:phenomenonTime ?effectEnd .\n" +
            "    }\n" +
            "    OPTIONAL {\n" +
            "        ?effectObservation2 sosa:hasResult ?startEvent .\n" +
            "        ?startEvent a s:Event .\n" +
            "        ?effectObservation2 sosa:phenomenonTime ?effectStart .\n" +
            "    }\n" +
            "    \n" +
            "    # cause State\n" +
            "    ?causeState a sense:State .\n" +
            "    ?causeState sense:hasStateType ?causeStateType .\n" +
            "    ?causeObservation sosa:hasResult ?causeState .\n" +
            "    ?causeState s:hasEndEvent ?causeEndEvent .\n" +
            "    ?causeState s:hasStartEvent ?causeStartEvent .\n" +
            "    ?causeSensor sosa:madeObservation ?causeObservation .\n" +
            "    ?causePlatform sosa:hosts ?causeSensor .\n" +
            "    ?causeParentPlatform sosa:hosts ?causePlatform .\n" +
            "    \n" +
            "    OPTIONAL {\n" +
            "        ?causeObservation1 sosa:hasResult ?causeEndEvent .\n" +
            "        ?causeEndEvent a s:Event .\n" +
            "        ?causeObservation1 sosa:phenomenonTime ?causeEnd .\n" +
            "    }\n" +
            "    OPTIONAL {\n" +
            "        ?causeObservation2 sosa:hasResult ?causeStartEvent .\n" +
            "        ?causeStartEvent a s:Event .\n" +
            "        ?causeObservation2 sosa:phenomenonTime ?causeStart .\n" +
            "    }\n" +
            "\n" +
            "    {\n" +
            "        SELECT ?causeStateType ?effectStateType ?causalityType ?platformRequirement ?temporalRelation ?stc WHERE { \n" +
            "            ?stc a s:StateTypeCausality .\n" +
            "            ?stc s:cause ?causeStateType .    \n" +
            "            ?stc s:effect ?effectStateType .\n" +
            "            ?stc s:hasCausalRelation ?causalityType .\n" +
            "            ?stc s:hasTemporalRelation ?temporalRelation .\n" +
            "            ?stc s:hasTopologicalRelation ?platformRequirement .\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    BIND(\n" +
            "        IF(?platformRequirement = s:samePlatform, ?causePlatform = ?effectPlatform, \n" +
            "        IF(?platformRequirement = s:parentPlatform, ?causeParentPlatform = ?effectPlatform, \n" +
            "        IF(?platformRequirement = s:siblingPlatform, ?causeParentPlatform = ?effectParentPlatform, true))) as ?Platform_filter\n" +
            "    )\n" +
            "    BIND(\n" +
            "        IF(?temporalRelation = s:overlaps, ?causeStart <= ?effectStart && ?effectStart <= ?causeEnd, \n" +
            "        IF(?temporalRelation = s:before, ?causeStart <= ?effectStart, true)) as ?temporal_filter\n" +
            "    )\n" +
            "\n" +
            "    FILTER(?Platform_filter && ?temporal_filter) \n" +
            "}\n";
}
