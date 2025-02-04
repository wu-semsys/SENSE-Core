package sense.explanationinterface.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
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
        @JsonProperty("base-uri")
        public String baseUri;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExplanationInterfaceConfig {
        public int port;

        @JsonProperty("chatbot-integration")
        public ChatBotIntegrationConfig chatBotIntegration;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatBotIntegrationConfig {
        @JsonProperty("named-graph")
        public String namedGraph;
        public List<QueryModification> queries;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryModification {
        @JsonProperty("name")
        public String name;

        @JsonProperty("new-prefixes")
        public List<String> newPrefixes;

        @JsonProperty("new-statements")
        public List<String> newStatements;
    }

    public static Config load(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream;

        File externalFile = new File(filePath);
        if (externalFile.exists()) {
            inputStream = new FileInputStream(externalFile);
        } else {
            inputStream = Config.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException(filePath + " not found in classpath or as an external file");
            }
        }
        return mapper.readValue(inputStream, Config.class);
    }

}
