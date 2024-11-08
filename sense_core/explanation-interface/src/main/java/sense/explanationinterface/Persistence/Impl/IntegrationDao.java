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
        }
    }

    @Override
    public void integrateStaticChatbotData() throws UpdateExecutionException {
        initializeRepository();
        LOGGER.trace("integrateStaticChatbotData()");
        String[] staticQueries = {
                query.SENSOR_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.SENSOR_TYPE_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.STATE_TYPE_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.TEMPORAL_RELATION_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.TOPOLOGICAL_RELATION_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.CAUSAL_RELATION_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.EVENT_TYPE_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.OBSERVABLE_PROPERTY_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.PROCEDURE_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.RESULT_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph),
                query.STATE_TYPE_CAUSALITY_QUERY.replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph)
        };
        for (String sparqlQuery : staticQueries) {
            executeInsertQuery(sparqlQuery);
            LOGGER.trace("Successfully executed query: " + sparqlQuery);
        }
        LOGGER.info("Static queries have been successfully executed");
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
                    LOGGER.info("Event Type: {}, Observation: {}, State: {}", event.getEventType(), event.getObservation(), event.getState());
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
                    LOGGER.info("Event Type: {}, Observation: {}", event.getEventType(), event.getObservation());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving event data without state: {}", e.getMessage());
        }
        return event;
    }

    private void insertEventChatbotData(IntegrationDto integrationDto) {
        String sparqlQuery = query.EVENT_QUERY
                .replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph)
                .replaceAll("eventURI", integrationDto.getEventURI());
        executeInsertQuery(sparqlQuery);
    }

    private void insertStateChatbotData(String state) {
        String sparqlQuery = query.STATE_QUERY
                .replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph)
                .replaceAll("stateURI", state);
        executeInsertQuery(sparqlQuery);
    }

    private void insertObservationChatbotData(String observation) {
        String sparqlQuery = query.OBSERVATION_QUERY
                .replaceAll("namedGraph", config.explanationInterface.chatBotIntegration.namedGraph)
                .replaceAll("observationURI", observation);
        executeInsertQuery(sparqlQuery);
    }
}
