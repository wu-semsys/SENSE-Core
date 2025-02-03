package sense.explanationinterface.Service;


import sense.explanationinterface.Dto.ExplanationResponseDto;

import java.util.Map;

public interface ExplanationService {
    ExplanationResponseDto getExplanations(String datetimeStr) throws Exception;
    Map<String, Object> executeSparqlQuery(String sparqlQuery) throws Exception;

    ExplanationResponseDto getExplanations(String datetimeStr, String user) throws Exception;
}
