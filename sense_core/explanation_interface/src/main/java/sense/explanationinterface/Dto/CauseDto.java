package sense.explanationinterface.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseDto {
    private String value;
    private String sensor;
    private String startTime;
    private String endTime;
}
