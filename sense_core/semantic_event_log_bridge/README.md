# Semantic Event Log Bridge

This service is responsible for listening for detected events and publishing them in the semantic event log. Currently, only a GraphDB is supported as event log.

## Configuration

The Semantic Event Log Bridge service can be configured via a json file. The user provides the path to this configuration file when invoking the service. This section details the configuration structure.

### MQTT Configuration (`mqtt`)

This section configures the connection to the MQTT broker.

`host`: the host name of the MQTT broker.

`host`: the port of the MQTT broker.

`clientId`: sets the [ClientId](https://www.hivemq.com/blog/mqtt-essentials-part-3-client-broker-connection-establishment/#heading-what-is-client-id-in-connect-mqtt-packet) of the connection to the MQTT broker.

### Semantic Event Log Configuration (`event_log`)

This section configures the connection to the GraphDB instance that hosts the semantic model.

`host`: the host name of the GraphDB instance.

`port`: the port of the GraphDB instance.

`repository`: the [GraphDB repository](https://graphdb.ontotext.com/documentation/10.7/repositories-overview.html) that should host the resulting triples.

`event-graph` the URI of the named graph that should host the resulting triples.

### Examples

```json
{
    "mqtt": {
        "host": "mosquitto",
        "port": 1883,
        "clientId": "semantic-event-log-bridge"
    },
    "event_log": {
        "host": "knowledgebase",
        "port": 7200,
        "repository": "bifrost",
        "event-graph": "http://w3id.org/explainability/graph/bifrost_dynamic"
    }
}

```

