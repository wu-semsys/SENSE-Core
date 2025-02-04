package sense.explanationinterface.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sense.explanationinterface.Dto.CauseDto;
import sense.explanationinterface.Dto.EffectDto;
import sense.explanationinterface.Dto.ExplanationDto;
import sense.explanationinterface.Dto.ExplanationResponseDto;
import sense.explanationinterface.Entity.Cause;
import sense.explanationinterface.Entity.Effect;
import sense.explanationinterface.Entity.Explanation;
import sense.explanationinterface.Exception.NoStateFoundException;
import sense.explanationinterface.Persistence.ExplanationDao;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExplanationService implements sense.explanationinterface.Service.ExplanationService {
    @Autowired
    private ExplanationDao explanationDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplanationService.class);
    @Override
    public ExplanationResponseDto getExplanations(String datetimeStr) throws Exception {
        LOGGER.trace("getExplanations({})", datetimeStr);
        String stateToExplain = getStateToExplain(datetimeStr);

        if (stateToExplain == null) {
            throw new NoStateFoundException("No state found for the provided datetime");
        }

        List<Explanation> explanations = explanationDao.runSelectQuery(stateToExplain);

        List<ExplanationDto> explanationDtos = explanations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new ExplanationResponseDto(stateToExplain, explanationDtos);
    }

    @Override
    public Map<String, Object> executeSparqlQuery(String sparqlQuery) throws Exception {
        LOGGER.trace("executeSparqlQuery({})", sparqlQuery);
        return explanationDao.executeSparqlQuery(sparqlQuery);
    }

    private String getStateToExplain(String datetimeStr) throws Exception {
        LOGGER.trace("getStateToExplain({})", datetimeStr);
        return explanationDao.getStateToExplain(datetimeStr);
    }

    private ExplanationDto mapToDto(Explanation explanation) {
        LOGGER.trace("mapToDto({})", explanation);
        Cause cause = explanation.getCause();
        Effect effect = explanation.getEffect();

        CauseDto causeDto = new CauseDto(
                cause.getValue(),
                cause.getSensor(),
                cause.getStartTime(),
                cause.getEndTime()
        );

        EffectDto effectDto = new EffectDto(
                effect.getValue(),
                effect.getSensor(),
                effect.getStartTime(),
                effect.getEndTime()
        );

        return new ExplanationDto(causeDto, effectDto, explanation.getRelation());
    }
}
