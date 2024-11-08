package sense.explanationinterface.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationDto {

    private String eventURI;

    @Override
    public String toString() {
        return "IntegrationDto{" +
                "eventURI='" + eventURI + '\'' +
                '}';
    }
}
