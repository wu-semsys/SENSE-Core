package org.semsys;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

public class EventBroker {
    private Config config;
    private MqttClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBroker.class);
    private PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparing(Event::getDateTime));
    private static final int BUFFER_TIME_MS = 2 * 60 * 1000;
    private boolean isTestEnvironment;

    public EventBroker(String configFilePath) throws IOException {
        config = Config.load(configFilePath);

        String appEnv = System.getenv("APP_ENV");
        if ("test".equalsIgnoreCase(appEnv)) {
            LOGGER.info("Running for TEST Environment..");
            isTestEnvironment = true;
        } else {
            LOGGER.info("Running for LIVE Environment..");
            isTestEnvironment = false;
        }

        if (isTestEnvironment) {
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    processBufferedEvents();
                }
            }, 0, BUFFER_TIME_MS);
        }
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

                if (isTestEnvironment) {
                    Event event = new EventToStateCausalityDAO(message, config).getEventDateTime();
                    bufferEvent(event);
                } else {
                    processEvent(message);
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

    private void bufferEvent(Event event) {
        eventQueue.add(event);
    }


    private void processBufferedEvents() {
        LOGGER.info("Processing buffered events...");

        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            processEvent(event.getUri());
        }

        LOGGER.info("Finished processing all buffered events.");
    }


    private void processEvent(String message) {
        EventToStateCausalityDAO eventToStateCausalityDAO = new EventToStateCausalityDAO(message, config);
        eventToStateCausalityDAO.insertStartState();
        eventToStateCausalityDAO.insertNewEndState();
        eventToStateCausalityDAO.insertCausality();
    }


}
