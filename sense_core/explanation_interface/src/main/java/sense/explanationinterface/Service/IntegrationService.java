package sense.explanationinterface.Service;

import sense.explanationinterface.Dto.IntegrationDto;

import java.util.List;

public interface IntegrationService {

    void integrateDynamicChatbotData(IntegrationDto integrationDto);

    void integrateStaticChatbotData();

}
