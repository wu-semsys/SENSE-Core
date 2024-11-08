package sense.explanationinterface.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EffectDto {
    private String value;
    private String sensor;
    private String startTime;
    private String endTime;
}
