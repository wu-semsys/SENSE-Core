# Event-to-State Causality

This repository contains a Java-based application that connects to an MQTT broker, subscribes to a topic, and processes incoming messages to infer event-to-state causality. The inferred states are then inserted into a GraphDB repository.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Code Structure](#code-structure)
- [License](#license)

## Requirements

- Docker
- Java 17
- Maven 3.8.1 or higher
- MQTT Broker (e.g., Mosquitto)
- GraphDB instance

## Installation

Clone the repository:

```sh
git clone https://git.ai.wu.ac.at/sense/sense-core.git
cd event-to-state-causality
```
To run the application, build and run the Docker container or include it in a docker compose. Example to build and run the Docker container:

```sh
docker build -t event-to-state-causality .
docker run event-to-state-causality
```

Ensure that the MQTT broker and GraphDB are running and accessible.


## Configuration

The configuration for the application is defined in a JSON file:

```json
{
    "mqtt": {
        "host": "mosquitto",
        "port": 1883,
        "clientId": "event-to-state-causality"
    },
    "semantic-model": {
        "host": "knowledgebase",
        "port": 7200,
        "repository": "seehub",
        "namedGraph": "http://w3id.org/explainability/graph/seehub_dynamic"
    }
}

```

Ensure that the MQTT broker and GraphDB repository, host, and port are correctly set.

## Code Structure

- Config: Contains configuration details for MQTT broker and GraphDB.

- EventBroker: Handles connection to the MQTT broker and processes incoming messages.

- EventToStateCausalityDAO: Manages interactions with the GraphDB repository, including inserting states and causality relations.

- Main: Entry point of the application.

- Query: Contains SPARQL queries for interacting with the GraphDB repository.

## License
TODO