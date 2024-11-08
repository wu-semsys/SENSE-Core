# Explanation Engine

This contains a Python-based application that provides explanations for specific states identified from a semantic model. The application connects to a SPARQL endpoint to fetch causal relationships and returns them as JSON responses via a Flask-based API.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Code Structure](#code-structure)
- [Open API](#open-api)
- [License](#license)

## Requirements

- Docker
- Python 3.12
- SPARQL Endpoint (e.g., GraphDB instance)

## Installation

1. **Clone the repository:**

    ```sh
    git clone https://git.ai.wu.ac.at/sense/sense-core.git
    cd sense_core/explanation-engine
    ```

2. **Build and run the Docker container:**

    ```sh
    docker build -t explanation-engine .
    docker run -p 5001:5001 explanation-engine
    ```

    Ensure that the SPARQL endpoint is running and accessible.

## Configuration

The configuration for the application is defined in a JSON file (`config.json`):

```json
{
    "semantic-model": {
        "host": "knowledgebase",
        "port": 7200,
        "repository": "seehub"
    },
    "explanation-engine": {
        "port": 5001
    }
}
```

Ensure that the SPARQL endpoint's host, port, and repository are correctly set.

## Code Structure

- **main.py**: Entry point of the application. Sets up the Flask server and defines the API endpoints.
- **explanationqueries/causal_path_identification.py**: Contains the logic for querying the SPARQL endpoint to identify causal paths and states to explain.
- **config.json**: Configuration file containing settings for the semantic model (e.g., GraphDB) and the explanation engine.

## Open API

### Endpoint: GET /explanations

Fetches explanations for a specified state at the nearest given datetime

**Request Parameters:**

- `datetime` (required): The datetime string to identify the state to explain.

**Example Request:**

`GET /explanations?datetime=2023-04-09T12:00:00`

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

## License

TODO