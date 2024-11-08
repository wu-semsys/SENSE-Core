package sense.explanationinterface.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Config {
    @JsonProperty("semantic-model")
    public SemanticModelConfig semanticModel;

    @JsonProperty("explanation-interface")
    public ExplanationInterfaceConfig explanationInterface;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SemanticModelConfig {
        public String host;
        public int port;
        public String repository;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExplanationInterfaceConfig {
        public int port;

        @JsonProperty("chatbot-integration")
        public ChatBotIntegrationConfig chatBotIntegration;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatBotIntegrationConfig {
        @JsonProperty("named-graph")
        public String namedGraph;
    }

    public static Config load(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException(filePath + " not found in classpath");
            }
            return mapper.readValue(inputStream, Config.class);
        }
    }
}
