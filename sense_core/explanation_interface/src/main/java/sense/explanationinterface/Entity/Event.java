package sense.explanationinterface.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private String eventType;
    private String observation;
    private String state;

    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventType + '\'' +
                ", observation='" + observation + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
