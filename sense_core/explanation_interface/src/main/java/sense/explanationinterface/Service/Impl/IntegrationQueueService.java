package sense.explanationinterface.Service.Impl;

import jakarta.annotation.PostConstruct;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sense.explanationinterface.Dto.IntegrationDto;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class IntegrationQueueService {

    private final BlockingQueue<IntegrationDto> integrationQueue = new LinkedBlockingQueue<>();

    @Autowired
    private IntegrationService integrationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationQueueService.class);

    @PostConstruct
    public void startQueueProcessor() {
        new Thread(() -> {
            while (true) {
                try {
                    IntegrationDto integrationDto = integrationQueue.take();
                    integrationService.integrateDynamicChatbotData(integrationDto);
                } catch (UpdateExecutionException e) {
                    LOGGER.error("Error processing queued integration: {}", e.getMessage(), e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    public void add(IntegrationDto integrationDto) {
        integrationQueue.add(integrationDto);
    }
}

