package sense.explanationinterface.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cause {
    private String value;
    private String sensor;
    private String startTime;
    private String endTime;
    private String mitigation;
}
