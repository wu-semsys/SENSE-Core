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
    private String mitigation;

    public CauseDto(String value, String sensor, String startTime, String endTime) {
        this.value = value;
        this.sensor = sensor;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
