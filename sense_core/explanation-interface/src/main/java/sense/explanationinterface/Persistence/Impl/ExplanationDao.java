package sense.explanationinterface.Persistence.Impl;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import sense.explanationinterface.Config.Config;
import sense.explanationinterface.Config.QueryConfig;
import sense.explanationinterface.Entity.Cause;
import sense.explanationinterface.Entity.Effect;
import sense.explanationinterface.Entity.Explanation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExplanationDao implements sense.explanationinterface.Persistence.ExplanationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplanationDao.class);
    private Config config;
    private QueryConfig queryConfig;
    private RepositoryManager repositoryManager;
    private org.eclipse.rdf4j.repository.Repository repository;

    public ExplanationDao() throws IOException {
       config = Config.load("config.json");
       queryConfig = new QueryConfig();
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
    public String getStateToExplain(String datetimeStr) throws Exception {
        LOGGER.info("getStateToExplain({})", datetimeStr);
        initializeRepository();
        String query = queryConfig.STATE_TO_EXPLAIN.replaceAll("datetime_str", datetimeStr);
        LOGGER.info(query);
        try (RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(query);

            try (TupleQueryResult result = tupleQuery.evaluate()) {
                if (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value v = bindingSet.getValue("v");
                    if (v != null) {
                        String value = v.stringValue();
                        return value.substring(value.indexOf("#") + 1);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while querying the SPARQL endpoint: {}", e.getMessage(), e);
            throw e;
        }

        return null;
    }

    @Override
    public List<Explanation> runSelectQuery(String stateToExplain) throws Exception {
        LOGGER.info("runSelectQuery({})", stateToExplain);
        initializeRepository();
        String query = queryConfig.EXPLANATION_SELECT_QUERY.replaceAll("StateToExplain", stateToExplain);
        LOGGER.info(query);
        List<Explanation> explanations = new ArrayList<>();

        try (RepositoryConnection connection = repository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(query);

            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    Cause cause = new Cause();
                    cause.setValue(bindingSet.getValue("cause").stringValue());
                    cause.setSensor(bindingSet.getValue("causesensor").stringValue());
                    cause.setStartTime(bindingSet.getValue("cset").stringValue());
                    cause.setEndTime(bindingSet.getValue("ceet").stringValue());

                    Effect effect = new Effect();
                    effect.setValue(bindingSet.getValue("effect").stringValue());
                    effect.setSensor(bindingSet.getValue("effectsensor").stringValue());
                    effect.setStartTime(bindingSet.getValue("set").stringValue());
                    effect.setEndTime(bindingSet.getValue("eet").stringValue());

                    String relation = bindingSet.getValue("relation").stringValue();

                    Explanation explanation = new Explanation();
                    explanation.setCause(cause);
                    explanation.setEffect(effect);
                    explanation.setRelation(relation);

                    explanations.add(explanation);
                }
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while querying the SPARQL endpoint: {}", e.getMessage(), e);
            throw e;
        }

        return explanations;
    }
}
