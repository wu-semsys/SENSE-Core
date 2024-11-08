package sense.explanationinterface.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sense.explanationinterface.Dto.ExplanationResponseDto;
import sense.explanationinterface.Exception.NoStateFoundException;
import sense.explanationinterface.Service.Impl.ExplanationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/explanations")
public class ExplanationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplanationController.class);

    @Autowired
    private ExplanationService explanationService;

    @GetMapping()
    public ResponseEntity<?> getExplanations(@RequestParam(value = "datetime", required = false) String datetimeStr) {
        LOGGER.info("GET Request /explanations?datetime={}", datetimeStr);

        if (datetimeStr == null || datetimeStr.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "datetime parameter is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            ExplanationResponseDto response = explanationService.getExplanations(datetimeStr);
            LOGGER.info("Explanations results = {}", response);

            return ResponseEntity.ok(response);
        } catch (NoStateFoundException e) {
            LOGGER.error("No state found: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        } catch (Exception e) {
            LOGGER.error("An error occurred: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
