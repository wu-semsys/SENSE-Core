package sense.explanationinterface.Service;


import sense.explanationinterface.Dto.ExplanationResponseDto;

public interface ExplanationService {
    ExplanationResponseDto getExplanations(String datetimeStr) throws Exception;
}
