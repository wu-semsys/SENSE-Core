package sense.explanationinterface.Persistence.Impl;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.springframework.stereotype.Repository;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sense.explanationinterface.Config.Config;
import sense.explanationinterface.Config.QueryConfig;
import sense.explanationinterface.Dto.IntegrationDto;
import sense.explanationinterface.Entity.Event;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class IntegrationDao implements sense.explanationinterface.Persistence.IntegrationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationDao.class);
    private RepositoryManager repositoryManager;
    private org.eclipse.rdf4j.repository.Repository repository;
    private Config config;
    private QueryConfig query;

    public IntegrationDao() throws IOException {
        config = Config.load("config.json");
        query = new QueryConfig();
    }

    private synchronized void initializeRepository() {
        if (repositoryManager == null || repository == null) {
            try {
                LOGGER.trace("Initializing connection to graphdb");
                repositoryManager = new RemoteRepositoryManager("http://" + config.semanticModel.host + ":" + config.semanticModel.port);
                repositoryManager.init();
                LOGGER.trace("Getting repository status from graphdb");
                repository = repositoryManager.getRepository(config.semanticModel.repository);
            } catch (Exception e) {
                LOGGER.error("Failed to initialize repository: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize repository", e);
            }
        }
    }

    @Override
    public void integrateDynamicChatbotData(IntegrationDto integrationDto) throws UpdateExecutionException {
        initializeRepository();
        LOGGER.trace("integrateDynamicChatbotData({})", integrationDto);
        boolean state = true;
        Event event = getEventData(integrationDto);
        if (event.getState() == null) {
            state = false;
            event = getEventDataWithoutState(integrationDto);
        }
        if (!state) {
            insertEventChatbotData(integrationDto);
            insertObservationChatbotData(event.getObservation());
        } else if (event.getEventType().equals("startEvent")) {
            insertEventChatbotData(integrationDto);
            insertObservationChatbotData(event.getObservation());
            insertStateChatbotData(event.getState());
        } else if (event.getEventType().equals("endEvent")) {
            insertEventChatbotData(integrationDto);
            insertObservationChatbotData(event.getObservation());
            insertStateChatbotData(event.getState());
        }
        insertChatbotData("RESULT", null);
    }

    @Override
    public void integrateStaticChatbotData() throws UpdateExecutionException {
        initializeRepository();
        LOGGER.trace("integrateStaticChatbotData()");

        String[] staticQueries = {
                "SENSOR_QUERY",
                "SENSOR_TYPE_QUERY",
                "STATE_TYPE_QUERY",
                "TEMPORAL_RELATION_QUERY",
                "TOPOLOGICAL_RELATION_QUERY",
                "CAUSAL_RELATION_QUERY",
                "EVENT_TYPE_QUERY",
                "OBSERVABLE_PROPERTY_QUERY",
                "PROCEDURE_QUERY",
                "STATE_TYPE_CAUSALITY_QUERY"
        };

        for (String queryName : staticQueries) {
            insertChatbotData(queryName, null);
        }

        LOGGER.info("Static chatbot data integration completed successfully.");
    }

    private void executeInsertQuery(String sparqlQuery) throws UpdateExecutionException {
        initializeRepository();
        LOGGER.trace("executeInsertQuery({})", sparqlQuery);
        try (RepositoryConnection connection = repository.getConnection()) {
            Update update = connection.prepareUpdate(sparqlQuery);
            update.execute();
        } catch (Exception e) {
            LOGGER.error("Error executing insert query: {}", sparqlQuery, e);
            throw new UpdateExecutionException("Failed to execute insert query: " + sparqlQuery, e);
        }
    }

    private Event getEventData(IntegrationDto integrationDto) {
        initializeRepository();
        LOGGER.trace("getEventData({})", integrationDto);
        Event event = new Event();
        try (RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(query.CHECK_EVENT_TYPE_QUERY
                    .replaceAll("eventURI", integrationDto.getEventURI()));
            try (TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
                for (BindingSet bindings : tupleQueryResult) {
                    event.setEventType(bindings.getValue("eventType").stringValue());
                    event.setObservation(bindings.getValue("observation").stringValue());
                    event.setState(bindings.getValue("state").stringValue());
                    LOGGER.trace("Event Type: {}, Observation: {}, State: {}", event.getEventType(), event.getObservation(), event.getState());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving event data: {}", e.getMessage());
        }
        return event;
    }

    private Event getEventDataWithoutState(IntegrationDto integrationDto) {
        initializeRepository();
        LOGGER.trace("getEventDataWithoutState({})", integrationDto);
        Event event = new Event();
        try (RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(query.CHECK_EVENT_TYPE_WITHOUT_STATE_QUERY
                    .replaceAll("eventURI", integrationDto.getEventURI()));
            try (TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
                for (BindingSet bindings : tupleQueryResult) {
                    event.setEventType(bindings.getValue("eventType").stringValue());
                    event.setObservation(bindings.getValue("observation").stringValue());
                    LOGGER.trace("Event Type: {}, Observation: {}", event.getEventType(), event.getObservation());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving event data without state: {}", e.getMessage());
        }
        return event;
    }

    /**
     * Inserts event data into the chatbot graph if a modification exists in config.json.
     *
     * @param integrationDto The DTO containing event information.
     */
    private void insertEventChatbotData(IntegrationDto integrationDto) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("eventURI", integrationDto.getEventURI());
        insertChatbotData("EVENT", placeholders);
    }

    /**
     * Inserts state data into the chatbot graph if a modification exists in config.json.
     *
     * @param state The state URI to insert.
     */
    private void insertStateChatbotData(String state) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("stateURI", state);
        insertChatbotData("STATE", placeholders);
    }

    /**
     * Inserts observation data into the chatbot graph if a modification exists in config.json.
     *
     * @param observation The observation URI to insert.
     */
    private void insertObservationChatbotData(String observation) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("observationURI", observation);
        insertChatbotData("OBSERVATION", placeholders);
    }

    private String getPredefinedQueryByName(String queryName) {
        LOGGER.trace("getPredefinedQueryByName({})", queryName);
        switch (queryName) {
            case "SENSOR":
                return query.SENSOR_QUERY;
            case "SENSOR_TYPE":
                return query.SENSOR_TYPE_QUERY;
            case "STATE_TYPE":
                return query.STATE_TYPE_QUERY;
            case "TEMPORAL_RELATION":
                return query.TEMPORAL_RELATION_QUERY;
            case "TOPOLOGICAL_RELATION":
                return query.TOPOLOGICAL_RELATION_QUERY;
            case "CAUSAL_RELATION":
                return query.CAUSAL_RELATION_QUERY;
            case "EVENT_TYPE":
                return query.EVENT_TYPE_QUERY;
            case "OBSERVABLE_PROPERTY":
                return query.OBSERVABLE_PROPERTY_QUERY;
            case "PROCEDURE":
                return query.PROCEDURE_QUERY;
            case "STATE_TYPE_CAUSALITY":
                return query.STATE_TYPE_CAUSALITY_QUERY;
            case "EVENT":
                return query.EVENT_QUERY;
            case "RESULT":
                return query.RESULT_QUERY;
            case "STATE":
                return query.STATE_QUERY;
            case "OBSERVATION":
                return query.OBSERVATION_QUERY;
            default:
                return null;
        }
    }
    private Config.QueryModification findModificationByName(String queryName) {
        List<Config.QueryModification> modifications = config.explanationInterface.chatBotIntegration.queries;
        if (modifications == null || modifications.isEmpty()) {
            return null;
        }
        for (Config.QueryModification mod : modifications) {
            if (mod.name.equalsIgnoreCase(queryName)) {
                return mod;
            }
        }
        return null;
    }

    private void insertChatbotData(String queryName, Map<String, String> dynamicPlaceholders) {
        Config.QueryModification modification = findModificationByName(queryName);
        if (modification == null) {
            LOGGER.trace("No modification found for insert query: {}. Skipping execution.", queryName);
            return;
        }

        LOGGER.info("Processing insert query modification for: {}", queryName);

        String predefinedQuery = getPredefinedQueryByName(queryName);
        if (predefinedQuery == null) {
            LOGGER.warn("Predefined insert query not found for name: {}. Skipping.", queryName);
            return;
        }

        StringBuilder mergedPrefixes = new StringBuilder();
        List<String> newPrefixes = modification.newPrefixes;
        if (newPrefixes != null && !newPrefixes.isEmpty()) {
            for (String prefix : newPrefixes) {
                mergedPrefixes.append(prefix).append("\n");
            }
        }

        StringBuilder mergedInsertStatements = new StringBuilder();
        List<String> newStatements = modification.newStatements;
        if (newStatements != null && !newStatements.isEmpty()) {
            for (String stmt : newStatements) {
                mergedInsertStatements.append(stmt).append("\n");
            }
        }

        String sparqlQuery = predefinedQuery
                .replace("{new-prefixes}", mergedPrefixes.toString())
                .replace("{new-statements}", mergedInsertStatements.toString())
                .replace("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph);

        if (dynamicPlaceholders != null && !dynamicPlaceholders.isEmpty()) {
            for (Map.Entry<String, String> entry : dynamicPlaceholders.entrySet()) {
                sparqlQuery = sparqlQuery.replace(entry.getKey(), entry.getValue());
            }
        }

        LOGGER.debug("Final SPARQL Insert Query for {}:\n{}", queryName, sparqlQuery);

        executeInsertQuery(sparqlQuery);
        LOGGER.trace("Successfully executed insert query: {}", queryName);
    }

}
