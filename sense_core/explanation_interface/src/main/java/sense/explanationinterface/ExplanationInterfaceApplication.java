package sense.explanationinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import sense.explanationinterface.Config.Config;

import java.io.IOException;


@SpringBootApplication
public class ExplanationInterfaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExplanationInterfaceApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        try {
            Config config = Config.load("config.json");
            int port = config.explanationInterface.port;
            return factory -> factory.setPort(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
