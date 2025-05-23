package sense.explanationinterface.Persistence;

import sense.explanationinterface.Entity.Explanation;

import java.util.List;
import java.util.Map;

public interface ExplanationDao {

    String getStateToExplain(String datetimeStr) throws Exception;
    List<Explanation> runSelectQuery(String stateToExplain) throws Exception;
    Map<String, Object> executeSparqlQuery(String sparqlQuery);
    String getStateToExplainWithUser(String datetimeStr, String user, String state) throws Exception;
    public List<Explanation> runSelectQuery(String stateToExplain, String user);
    String getSpecificStateToExplain(String datetimeStr, String state);
}
