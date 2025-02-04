package sense.explanationinterface.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sense.explanationinterface.Dto.IntegrationDto;
import sense.explanationinterface.Service.Impl.IntegrationQueueService;
import sense.explanationinterface.Service.IntegrationService;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/v1/api/integration")
public class IntegrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationController.class);
    @Autowired
    private IntegrationQueueService integrationQueueService;
    @Autowired
    private IntegrationService integrationService;
    private final AtomicBoolean isStaticDataIntegrated = new AtomicBoolean(false);

    @PostMapping
    private ResponseEntity<?> integration(@RequestBody IntegrationDto integrationDto) {
        LOGGER.info("POST Req /v1/api/integration Body: {}", integrationDto);
        try {

            if (isStaticDataIntegrated.compareAndSet(false, true)) {
                try {
                    LOGGER.info("Integrating Static Chatbot Data...");
                    integrationService.integrateStaticChatbotData();
                } catch (Exception e) {
                    LOGGER.error("Failed to integrate static chatbot data: {}", e.getMessage());
                    isStaticDataIntegrated.set(false);
                }
            }

            integrationQueueService.add(integrationDto);

            return ResponseEntity.accepted().body("Request received and processing started.");
        } catch (Exception e) {
            LOGGER.error("Failed to enqueue the integration request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start processing the integration.");
        }

    }

}
