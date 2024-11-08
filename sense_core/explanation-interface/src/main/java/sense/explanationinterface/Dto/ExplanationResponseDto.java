package sense.explanationinterface.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExplanationResponseDto {
    private String stateToExplain;
    private List<ExplanationDto> explanations;
}
