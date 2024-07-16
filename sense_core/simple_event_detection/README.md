# Simple Event Detection

This service is responsible for detecting simple events in the sensor time-series data.

## Supported Event Detection Semantics

Currently, the simple event detection only supports Signal Temporal Logic [1], an extension of temporal logics that allows continuous variables and time constraints. If the formula for a given event starts evaluating to true, the service creates a new event instance of this type and publishes on the event broker.

## Configuration

The Simple Event Detection service can be configured via a json file. The user provides the path to this configuration file when invoking the service. This section details the configuration structure.

### MQTT Configuration (`mqtt`)

This section configures the connection to the MQTT broker.

`host`: the host name of the MQTT broker.

`host`: the port of the MQTT broker.

`clientId`: sets the [ClientId](https://www.hivemq.com/blog/mqtt-essentials-part-3-client-broker-connection-establishment/#heading-what-is-client-id-in-connect-mqtt-packet) of the connection to the MQTT broker. The service will append a random string to the ClientId so that multiple connections from this host are possible.

### Semantic Model Configuration (`semantic-model`)

This section configures the connection to the GraphDB instance that hosts the semantic model.

`host`: the host name of the GraphDB instance.

`port`: the port of the GraphDB instance.

`repository`: the [GraphDB repository](https://graphdb.ontotext.com/documentation/10.7/repositories-overview.html) that hosts the relevant triples.

### Examples

```json
{
    "mqtt": {
        "host": "mosquitto",
        "port": 1883,
        "clientId": "monitoring-service"
    },
    "semantic-model": {
        "host": "knowledgebase",
        "port": 7200,
        "repository": "bifrost"
    }
}
```

## Viewing Events

Events can be viewed in the GraphDB frontend by visitng `http://localhost:7200`.
You can use the following SPARQL-Query for a list of detected events.

```sparql
PREFIX s: <http://w3id.org/explainability/sense#>
select ?event where { 
    ?event a s:Event .
}
```

## References

[1] Maler, Oded, and Dejan Nickovic. “Monitoring Temporal Properties of Continuous Signals.” In Formal Techniques, Modelling and Analysis of Timed and Fault-Tolerant Systems, edited by Yassine Lakhnech and Sergio Yovine, 152–66. Berlin, Heidelberg: Springer, 2004. https://doi.org/10.1007/978-3-540-30206-3_12.

