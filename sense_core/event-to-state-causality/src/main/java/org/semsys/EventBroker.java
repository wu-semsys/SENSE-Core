package org.semsys;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBroker {
    private Config config;
    private MqttClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBroker.class);

    public EventBroker() {
        config = new Config();
    }

    void connect() throws MqttException {
        LOGGER.info("Connecting to MQTT Broker");
        client = new MqttClient("tcp://" + config.BROKER_HOST + ":" + config.BROKER_PORT, "event-to-state");
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
                EventToStateCausalityDAO eventToStateCausalityDAO = new EventToStateCausalityDAO(message);
                eventToStateCausalityDAO.insertStartState();
                eventToStateCausalityDAO.insertNewEndState();
                eventToStateCausalityDAO.insertCausality();
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

}
