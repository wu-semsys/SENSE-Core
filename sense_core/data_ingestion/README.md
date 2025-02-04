# Data Ingestion

This service is responsible for relaying and integrating sensor readings into the SENSE system.

## Building the Docker Image
Execute the following command in this directory to build the docker image for this module:
```
docker build -t sense-core/data-ingestion:v1.0 .
```

## Configuration

The Data Ingestion service can be configured via a json file. The user provides the path to this configuration file when invoking the service. This section details the configuration structure.

### MQTT Configuration (`mqtt`)

This section configures the connection to the MQTT broker.

`host`: the host name of the MQTT broker.

`host`: the port of the MQTT broker.

`clientId`: sets the [ClientId](https://www.hivemq.com/blog/mqtt-essentials-part-3-client-broker-connection-establishment/#heading-what-is-client-id-in-connect-mqtt-packet) of the connection to the MQTT broker.

### Semantic Model Configuration (`semantic-model`)

This section configures the connection to the GraphDB instance that hosts the semantic model.

`host`: the host name of the GraphDB instance.

`port`: the port of the GraphDB instance.

`repository`: the [GraphDB repository](https://graphdb.ontotext.com/documentation/10.7/repositories-overview.html) that hosts the relevant triples.

### InfluxDB Configuration (`influxdb`)

This section configures the connection to the InfluxDB instance that hosts the sensor readings.

`host`: the host name of the InfluxDB instance.

`port`: the port of the InfluxDB instance.

`protocol`: the protocol used for querying the InfluxDB instance.

`bucket`: the [bucket](https://docs.influxdata.com/influxdb/v2/admin/buckets/) that hosts the sensor readings.

`org`: the [organization](https://docs.influxdata.com/influxdb/v2/admin/organizations/) that owns the sensor readings.

`token`: the [API token](https://docs.influxdata.com/influxdb/v2/admin/tokens/) for the data ingestion service.

### Time Configuration (`time`)

This section details which data the data ingestions service should relay to the event broker. There are two modes for this configuration - `live` and `replay`. 

`mode`: sets the mode of the time configuration. Possible values: `live` and `replay`. Deployments running in an environment that should obtain near real-time data from a system, should use the `live` mode. Deployments that should replay historical data (e.g., testing) should use the `replay` mode.

`sleepInSeconds`: sets the interval of queries. For example, with a `sleepInSeconds` value of 60, one query will be issued every minute. When using the `live` mode, this is also the time interval that will be (approximately) queried in a single query as the system clock will advance by this interval. *CAVEAT:* We did not yet test what happens if multiple sensor values are returned per query. Therefore, you should keep this, for now, lower than the sampling interval of the sensors when using `live` mode.

#### Replay mode (`mode` = `replay`)

`startAt`: determines the timestamp that the data ingestion will use as a starting point.

`stopAt`: determines the timestamp that the data ingestion will stop at when replaying data.

`stopAction`: configures how the system should handle reaching the `stopAt` timestamp. Possible values: `stop` and `repeat`. A value of `stop` indicates that the service should shut down and no longer forward sensor values to the event broker. A value of `repeat` indicates that the service should start against with the `startAt` timestamp. While doing so, the service issues a system message so that other services can react to the restarting of the scenario (e.g., dropping detected events). The `repeat` option is attractive for scenarios that host a "testing environment" that should always be available.

`deltaInSeconds`: configures what interval should be extracted per query. If `deltaInSeconds` is greater than `sleepInSeconds`, the scenario will be replayed faster than real time. *CAVEAT:* We did not yet test what happens if multiple sensor values are returned per query. Therefore, you should keep this, for now, lower than the sampling interval of the sensors when using `replay` mode.

### Examples

An example for replaying data from historical data from an InfluxDB.

```json
{
    "mqtt": {
        "host": "mosquitto",
        "port": 1883,
        "data-ingestion-id": "data-ingestion"
    },
    "semantic-model": {
        "host": "knowledgebase",
        "port": 7200,
        "repository": "seehub"
    },
    "influxdb": {
        "host": "influxdb",
        "port": 8086,
        "protocol": "http",
        "bucket": "seehub",
        "org": "seehub",
        "token": "secretadmintoken"
    },
    "data-ingestion": {
        "mode": "replay",
        "startAt": "2023-04-01T00:00:00",
        "stopAt": "2023-04-30T23:59:00",
        "stopAction": "repeat",
        "deltaInSeconds": "60",
        "sleepInSeconds": "60"
    }
}
```

An example for replaying live measurements to the event broker from an InfluxDB.

```json
{
    // same as above
    "data-ingestion": {
        "mode": "live",
        "sleepInSeconds": "60"
    }
}
```