package org.semsys;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventBroker {
    private Config config;
    private MqttClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBroker.class);

    public EventBroker(String configFilePath) throws IOException {
        config = Config.load(configFilePath);
    }

    void connect() throws MqttException {
        LOGGER.info("Connecting to MQTT Broker");
        client = new MqttClient("tcp://" + config.mqtt.host + ":" + config.mqtt.port, config.mqtt.clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                LOGGER.warn("Connection lost: {}", throwable.getMessage());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String message = new String(mqttMessage.getPayload());
                LOGGER.info("Message Arrived. Topic: {}, Message: {}", s, message);
                EventToStateCausalityDAO eventToStateCausalityDAO = new EventToStateCausalityDAO(message, config);
                eventToStateCausalityDAO.insertStartState();
                eventToStateCausalityDAO.insertNewEndState();
                eventToStateCausalityDAO.insertCausality();

                if (config.chatbotBridge != null && config.chatbotBridge.url != null && !config.chatbotBridge.url.isEmpty()) {
                    LOGGER.info("Sending message to Chatbot Bridge URL: {}", config.chatbotBridge.url);
                    sendMessageToChatbotBridge(config.chatbotBridge.url, message);
                } else {
                    LOGGER.info("No Chatbot Bridge URL configured.");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                LOGGER.trace("Delivery Complete: {}", iMqttDeliveryToken.isComplete());
            }
        });
    }

    public void subscribe(String topic) throws MqttException {
        LOGGER.info("Subscribed to topic: {}", topic);
        client.subscribe(topic);
    }

    private void sendMessageToChatbotBridge(String url, String message) throws IOException {
        LOGGER.trace("sendMessageToChatbotBridge({}, {})", url, message);

        IntegrationDto integrationDto = new IntegrationDto();
        integrationDto.setEventURI(message);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(integrationDto);

        URL chatbotUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) chatbotUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        LOGGER.trace("Response Code from Chatbot Bridge: {}", responseCode);

        if (responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK) {
            LOGGER.error("Failed to send message to Chatbot Bridge. Response code: {}", responseCode);
        }
    }

}
