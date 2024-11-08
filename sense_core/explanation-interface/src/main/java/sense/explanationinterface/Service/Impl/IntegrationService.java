package sense.explanationinterface.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sense.explanationinterface.Dto.IntegrationDto;
import sense.explanationinterface.Persistence.IntegrationDao;

@Service
public class IntegrationService implements sense.explanationinterface.Service.IntegrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationService.class);
    @Autowired
    private IntegrationDao integrationDao;

    @Override
    public void integrateDynamicChatbotData(IntegrationDto integrationDto) {
        LOGGER.trace("integrateDynamicChatbotData({})", integrationDto);
        integrationDao.integrateDynamicChatbotData(integrationDto);
    }

    @Override
    public void integrateStaticChatbotData() {
        LOGGER.trace("integrateStaticChatbotData()");
        integrationDao.integrateStaticChatbotData();
    }
}
