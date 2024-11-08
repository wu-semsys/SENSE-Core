package sense.explanationinterface.Persistence;

import sense.explanationinterface.Entity.Explanation;

import java.util.List;
import java.util.Map;

public interface ExplanationDao {

    String getStateToExplain(String datetimeStr) throws Exception;
    List<Explanation> runSelectQuery(String stateToExplain) throws Exception;
    Map<String, Object> executeSparqlQuery(String sparqlQuery);
}
