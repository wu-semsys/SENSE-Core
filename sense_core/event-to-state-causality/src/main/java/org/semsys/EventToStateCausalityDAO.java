package org.semsys;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

public class EventToStateCausalityDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventToStateCausalityDAO.class);
    private RepositoryManager manager;
    private Repository repository;
    private Config config = new Config();
    private Query query = new Query();
    private String eventURI;
    private String eventSensor;
    private String startedStateType;
    private String observedProperty;
    private String event;

    public EventToStateCausalityDAO(String eventURI) {
        if (manager == null || repository == null) {
            LOGGER.trace("initialize connection to graphdb");
            manager = new RemoteRepositoryManager(config.REPOSITORY_URL);
            manager.init();
            LOGGER.trace("getting repository status from graphdb");
            repository = manager.getRepository(config.REPOSITORY_NAME);

        }
        this.eventURI = eventURI;
        this.event = eventURI.split("_")[1];
    }

    public void insertStartState(){
        getEventData();
        insertNewStartState();
    }

    private void getEventData() {
        LOGGER.trace("getEventData({})", eventURI);
        RepositoryConnection connection = repository.getConnection();
        String query = this.query.COLLECT_EVENT_DATA.replace("eventURI", eventURI);
        TupleQuery tupleQuery = connection.prepareTupleQuery(query);
        TupleQueryResult tupleQueryResult = tupleQuery.evaluate();
        try {
            for (BindingSet bindings : tupleQueryResult) {
                eventSensor = bindings.getBinding("eventSensor").getValue().stringValue();
                startedStateType = bindings.getBinding("startedStateType").getValue().stringValue();
                observedProperty = bindings.getBinding("observedProperty").getValue().stringValue();
                LOGGER.trace("Found eventSensor {}, startedStateType {} and observedProperty {}",
                        eventSensor, startedStateType, observedProperty);
            }
        } finally {
            LOGGER.trace("Closing connection to graphdb..");
            connection.close();
        }
    }

    private void insertNewStartState() {
        LOGGER.trace("insertNewStartState({})", eventURI);
        RepositoryConnection connection = repository.getConnection();
        try {
            if (startedStateType != null) {
                String query = insertQueryManipulation();
                Update update = connection.prepareUpdate(query);
                update.execute();
                LOGGER.info("Insert new start state query executed successfully.");
            }
        } catch (Exception e) {
            LOGGER.error("Error executing insert new start state query: ", e);
        } finally {
            connection.close();
        }
    }

    public void insertNewEndState() {
        LOGGER.trace("insertNewEndState({})", eventURI);
        RepositoryConnection connection = repository.getConnection();
        try {
            String query = this.query.ADD_NEW_END_STATE.replaceAll("eventURI", eventURI);
            Update update = connection.prepareUpdate(query);
            update.execute();
            LOGGER.info("Insert new end state query executed successfully.");
        } catch (Exception e) {
            LOGGER.error("Error executing insert new end state query: ", e);
        } finally {
            connection.close();
        }
    }

    private String insertQueryManipulation() {
		UUID uuid = UUID.randomUUID();
        String result = query.ADD_NEW_START_STATE
                .replaceAll("eventURI2", eventURI)
                .replaceAll("startedStateType", startedStateType.split("#")[1])
                .replaceAll("eventURI", uuid.toString());
        return result;
    }

    public void insertCausality() {
        LOGGER.trace("insertCausality({})", eventURI);
        RepositoryConnection connection = repository.getConnection();
        try {
            String query = this.query.ADD_CAUSALITY;
            Update update = connection.prepareUpdate(query);
            update.execute();
            LOGGER.info("Insert causality query executed successfully.");
        } catch (Exception e) {
            LOGGER.error("Error executing insert causality query: ", e);
        } finally {
            connection.close();
        }
    }
}

