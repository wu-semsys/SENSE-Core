package org.semsys;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide the path to the configuration file.");
            System.exit(1);
        }

        String configFilePath = args[0];

        String topic = "events/state";

        try {

            EventBroker connector = new EventBroker(configFilePath);
            connector.connect();
            connector.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}