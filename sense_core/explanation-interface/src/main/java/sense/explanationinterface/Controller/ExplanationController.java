package sense.explanationinterface.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> getExplanations(@RequestParam(value = "datetime") String datetimeStr) {
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

    @PostMapping("/sparql")
    public ResponseEntity<?> executeSparqlQuery(@RequestBody String sparqlQuery) {
        LOGGER.info("POST Request /sparql with query: {}", sparqlQuery);

        if (sparqlQuery == null || sparqlQuery.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "SPARQL query is required in the request body");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Map<String, Object> results = explanationService.executeSparqlQuery(sparqlQuery);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid query: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (UnsupportedOperationException e) {
            LOGGER.error("Unsupported operation: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);
        } catch (Exception e) {
            LOGGER.error("An error occurred while executing SPARQL query: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to execute SPARQL query: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}