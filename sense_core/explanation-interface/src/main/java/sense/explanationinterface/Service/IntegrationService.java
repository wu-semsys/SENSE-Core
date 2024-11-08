package sense.explanationinterface.Service;

import sense.explanationinterface.Dto.IntegrationDto;

public interface IntegrationService {

    void integrateDynamicChatbotData(IntegrationDto integrationDto);

    void integrateStaticChatbotData();

}
