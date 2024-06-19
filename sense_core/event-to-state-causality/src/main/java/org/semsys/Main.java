package org.semsys;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static void main(String[] args) {

        String topic = "events/state";

        EventBroker connector = new EventBroker();
        try {
            connector.connect();
            connector.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}