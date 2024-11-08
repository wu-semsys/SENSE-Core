package sense.explanationinterface.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExplanationDto {
    private CauseDto cause;
    private EffectDto effect;
    private String relation;
}
