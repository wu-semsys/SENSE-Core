package sense.explanationinterface.Persistence;

import sense.explanationinterface.Entity.Explanation;

import java.util.List;

public interface ExplanationDao {

    String getStateToExplain(String datetimeStr) throws Exception;
    List<Explanation> runSelectQuery(String stateToExplain) throws Exception;
}
