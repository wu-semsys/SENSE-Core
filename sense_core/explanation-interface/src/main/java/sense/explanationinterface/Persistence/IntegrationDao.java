package sense.explanationinterface.Persistence;

import sense.explanationinterface.Dto.IntegrationDto;

public interface IntegrationDao {

    void integrateDynamicChatbotData(IntegrationDto integrationDto);

    void integrateStaticChatbotData();

}
