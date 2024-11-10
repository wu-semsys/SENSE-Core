# Explanation Interface

The **Explanation Interface** is a Spring Boot-based Java application designed to provide explanations for specific states identified from a semantic model. It connects to a SPARQL endpoint to fetch causal relationships and exposes these explanations through a RESTful API. Additionally, it offers integration capabilities to process and enqueue data for dynamic chatbot interactions.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
  - [Integration Endpoint](#integration-endpoint)
  - [Explanations Endpoint](#explanations-endpoint)
  - [SPARQL Query Endpoint](#sparql-query-endpoint)
- [License](#license)

## Overview

The **Explanation Interface** serves as a bridge between semantic models and applications requiring detailed explanations of specific states. By leveraging SPARQL queries, it identifies causal paths and states, presenting them in structured JSON responses. The application also supports dynamic integration of chatbot data, ensuring real-time responsiveness and adaptability.

## Features

- **RESTful API**: Exposes endpoints for fetching explanations and integrating data.
- **SPARQL Integration**: Connects to SPARQL endpoints (e.g., GraphDB) to execute queries.
- **Data Integration Queue**: Manages and processes integration requests asynchronously.
- **Configurable**: Adjustable configurations via `config.json`.
- **Logging**: Comprehensive logging using SLF4J for monitoring and debugging.

## Requirements

- **Java**: JDK 17 or later
- **Maven**: 3.6.0 or later
- **SPARQL Endpoint**: Accessible SPARQL endpoint (e.g., GraphDB instance)
- **Docker** (optional): For containerized deployment

## Installation

### 1. Clone the Repository

```sh
git clone https://git.ai.wu.ac.at/sense/sense-core.git
cd sense_core/explanation-interface
```

### 2. Build the Application

Using Maven:

```sh
mvn clean install
```

### 3. (Optional) Build and Run with Docker

If you prefer running the application in a Docker container:

1. **Build the Docker Image**

    ```sh
    docker build -t explanation-interface .
    ```

2. **Run the Docker Container**

    ```sh
    docker run -p 8080:8080 explanation-interface
    ```

    Ensure that the SPARQL endpoint is running and accessible from within the Docker container.

## Configuration

The application configuration is managed through a `config.json` file located in the `src/main/resources` directory. This file defines settings for the semantic model and the explanation interface.

### Sample `config.json`

```json
{
    "semantic-model": {
        "host": "localhost",
        "port": 7200,
        "repository": "seehub",
        "base-uri": "http://example.org/base#"
    },
    "explanation-interface": {
        "port": 8080,
        "chatbot-integration": {
            "named-graph": "http://example.org/chatbot#",
            "queries": [
                {
                    "name": "EVENT",
                    "new-prefixes": [
                        "PREFIX ex: <http://example.org/ex#>"
                    ],
                    "new-statements": [
                        "ex:eventURI ex:hasType ex:startEvent ."
                    ]
                },
                {
                    "name": "STATE",
                    "new-prefixes": [
                        "PREFIX ex: <http://example.org/ex#>"
                    ],
                    "new-statements": [
                        "ex:stateURI ex:hasValue \"active\" ."
                    ]
                }
            ]
        }
    }
}
```

### Configuration Parameters

- **semantic-model**
  - `host`: Hostname or IP address of the SPARQL endpoint.
  - `port`: Port number of the SPARQL endpoint.
  - `repository`: Name of the repository in the SPARQL endpoint.
  - `base-uri`: Base URI used in SPARQL queries.

- **explanation-interface**
  - `port`: Port on which the Spring Boot application will run.
  - **chatbot-integration**
    - `named-graph`: The named graph URI for chatbot data.
    - `queries`: List of query modifications for data integration.
      - `name`: Identifier for the query.
      - `new-prefixes`: List of new prefixes to include in the SPARQL queries.
      - `new-statements`: List of new SPARQL statements to insert.

## Running the Application

### Using Maven

```sh
mvn spring-boot:run
```

The application will start on the port specified in `config.json` (default is `8080`).

### Using the JAR File

After building the application, you can run the JAR file directly:

```sh
java -jar target/explanation-interface-0.0.1-SNAPSHOT.jar
```

### Docker

If you built the Docker image, ensure it's running as described in the [Installation](#installation) section.

## API Documentation

The application exposes several RESTful endpoints for integration and explanation purposes.

### Integration Endpoint

**Endpoint:** `POST /v1/api/integration`

**Description:** Receives integration requests to process and enqueue data for dynamic chatbot interactions.

**Request Body:**

```json
{
    "eventURI": "http://example.org/events/event123"
}
```

**Response:**

- **202 Accepted**

    ```json
    "Request received and processing started."
    ```

- **500 Internal Server Error**

    ```json
    "Failed to start processing the integration."
    ```


### Explanations Endpoint

**Endpoint:** `GET /v1/api/explanations`

**Description:** Fetches explanations for a specified state at the nearest given datetime.

**Request Parameters:**

- `datetime` (required): The datetime string to identify the state to explain (e.g., `2023-04-09T12:00:00`).

**Example Request:**

```sh
curl "http://localhost:8080/v1/api/explanations?datetime=2023-04-09T12:00:00"
```

**Example Response:**

```json
{
    "stateToExplain": "Violation_State_UUID",
    "explanations": [
        {
            "cause": {
                "value": "http://w3id.org/explainability/sense#Violation_State_UUID",
                "sensor": "http://example.org/projectName#Sensor",
                "startTime": "2023-04-09T11:00:00Z",
                "endTime": "2023-04-09T11:30:00Z"
            },
            "effect": {
                "value": "http://w3id.org/explainability/sense#Violation_State_UUID",
                "sensor": "http://example.org/projectName#Sensor",
                "startTime": "2023-04-09T11:00:00Z",
                "endTime": "2023-04-09T11:30:00Z"
            },
            "relation": "http://w3id.org/explainability/sense#causes"
        }
    ]
}
```

**Error Responses:**

- **400 Bad Request**

    ```json
    {
        "error": "datetime parameter is required"
    }
    ```

- **404 Not Found**

    ```json
    {
        "error": "No state found for the provided datetime"
    }
    ```

- **500 Internal Server Error**

    ```json
    {
        "error": "An unexpected error occurred."
    }
    ```

### SPARQL Query Endpoint

**Endpoint:** `POST /v1/api/explanations/sparql`

**Description:** Executes a SPARQL query against the configured SPARQL endpoint.

**Request Body:**

```text
SELECT ?s ?p ?o WHERE {
    ?s ?p ?o .
} LIMIT 10
```

**Response:**

- **200 OK**

    ```json
    {
        "head": {
            "vars": ["s", "p", "o"]
        },
        "results": {
            "bindings": [
                {
                    "s": "http://example.org/resource1",
                    "p": "http://example.org/property1",
                    "o": "Value1"
                },
                ...
            ]
        }
    }
    ```

- **400 Bad Request**

    ```json
    {
        "error": "Malformed SPARQL query: Syntax error..."
    }
    ```

- **501 Not Implemented**

    ```json
    {
        "error": "Graph queries are not supported in this endpoint."
    }
    ```

- **500 Internal Server Error**

    ```json
    {
        "error": "Failed to execute SPARQL query: <error message>"
    }
    ```

**Example Request:**

```sh
curl -X POST http://localhost:8080/v1/api/explanations/sparql \
     -H "Content-Type: application/sparql-query" \
     -d 'SELECT ?s ?p ?o WHERE { ?s ?p ?o . } LIMIT 10'
```

## License

TODO
