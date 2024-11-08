package sense.explanationinterface.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Explanation {
    private Cause cause;
    private Effect effect;
    private String relation;
}
