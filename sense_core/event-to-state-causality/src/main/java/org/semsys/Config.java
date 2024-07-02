package org.semsys;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Config {
    @JsonProperty("mqtt")
    public MQTTConfig mqtt;
    @JsonProperty("semantic-model")
    public SemanticModelConfig semanticModel;

    public static class MQTTConfig {
        public String host;
        public int port;
        public String clientId;
    }

    public static class SemanticModelConfig {
        public String host;
        public int port;
        public String repository;
        public String namedGraph;
    }

    public static Config load(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Config.class);
    }
}
