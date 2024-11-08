package org.semsys;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    @JsonProperty("mqtt")
    public MQTTConfig mqtt;
    @JsonProperty("semantic-model")
    public SemanticModelConfig semanticModel;
    @JsonProperty("explanation-interface")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ExplanationInterfaceConfig explanationInterface;
		
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MQTTConfig {
        public String host;
        public int port;
        @JsonProperty("event-to-state-causality-id")
        public String clientId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SemanticModelConfig {
        public String host;
        public int port;
        public String repository;
        @JsonProperty("named-graph")
        public String namedGraph;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatBotIntegrationConfig {
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExplanationInterfaceConfig {
        @JsonProperty("chatbot-integration")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public ChatBotIntegrationConfig chatBotIntegration;
    }

    public static Config load(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Config.class);
    }
}
