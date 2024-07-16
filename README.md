# Sense Core - Introduction

The SENSE Core is a result of the Semantics-based Explanation of Cyber-physical Systems (SENSE) research project. For basic information, please refer to the [SENSE homepage](https://sense-project.net/). The SENSE Core is a container-based application. It is structured and implemented according to the [Auditable SENSE Architecture](https://sense-project.net/wp-content/uploads/2024/06/D3_1_Auditable-SENSE-architecture.pdf).

This repository primaraliy contains the source code of all SENSE Core modules, and instructions (Dockerfiles) for building images for each of the modules. We also provide pre-built image files. However, you currently need access to container registry at [registry.ai.wu.ac.at](registry.ai.wu.ac.at).

## Table of Contents

- [TODOs](#todos)
- [SENSE Core Structure](#sense-core-structure)
- [Instantiation and Execution](#instantiation-and-execution)
- [Structure of the Repository](#structure-of-the-repository)
- [Named Graphs and Namespaces](#named-graphs-and-namespaces)
- [Configuration Files](#configuration-files)
- [GitLAB CI/CD](#gitlab-cicd)
- [License](#license)
- [References](#references)
- [Contributing](#contributing)
- [Contact](#contact)

## TODOs

- [ ] License?
- [ ] Relevant References
- [ ] Repository for a demo instantiation

## SENSE Core Structure

This repository provides the necessary modules/services making up the SENSE Core. Note that the SENSE Core itself is not an executable application as it lacks use-case specific system data. For information on how to create an instantiation of the SENSE Core, please refer to Section [Instantiation and Execution](#instantiation-and-execution).

![SENSE Core Container Structure](./doc/SENSE-Core-C4-Model-Level2-ContainerDiagram.png)

The structure and interdependencies of the SENSE Core modules are as follows:

- **data-ingestion:** This module is responsible for feeding sensor readings into the SENSE system. It currently supports data import from an 
InfluxDB time-series database, as this often available in existing CPSs. The module can be configured to replay data from the time-series database for testing purposes. Alternatively, it can be configured to ingest data that is added to the time-series database into the SENSE system for "live" operation. [Further Information](sense_core/data_ingestion/README.md)
- **data-and-event-broker:** This module provides a [Mosquitto MQTT broker](https://mosquitto.org/) for message exchange between modules as indicated the figure above. It uses the official [eclipse-mosquitto](https://hub.docker.com/_/eclipse-mosquitto) docker image. Hence, no Dockerfile or source code is provided for the data-and-event-broker in this repository.
- **simple-event-detection:** This module is responsible for detecting simple events in the sensor time-series data. Events can currently be specificatied in Signal Temporal Logic (STL). [Further Information](sense_core/simple_event_detection/README.md)
- **semantic-event-log-bridge:** This module is responsible for listening for detected events and publishing them in the semantic event log, resides with in the knowledgebase. Currently, only a GraphDB is supported as event log. [Further Information](sense_core/semantic_event_log_bridge/README.md)
- **knowledgebase:** This module is responsible for providing a semantic data storage to the SENSE system. It initializes the semantic data storage [GraphDB](https://graphdb.ontotext.com/) with a repository, named graphs, and ttl files as defined in the configuration. [Further Information](sense_core/knowledgebase/README.md)
- **event-to-state-causality:** This module contains a Java-based application that connects to te data-and-event-broker, subscribes to a topic, and processes incoming messages to infer event-to-state causality. The inferred states are then inserted into the knowledgebase. [Further Information](sense_core/event-to-state-causality/README.md)
- **explanation-generation:** This module contains a Python-based application that provides explanations for specific states identified from a semantic model. The application connects to a SPARQL endpoint to fetch causal relationships and returns them as JSON responses via a Flask-based API. [Further Information](sense_core/explanation-engine/README.md)

## System Requirements
The system requirements below were obtained by running the [Demo Instantiation](https://git.ai.wu.ac.at/sense/seehub) (TODO: Replace this link, which directs to our internal seehub demo use case, to the public BIFROST Demo instantiation) via the following commands:
 
```
docker ps -s # virtual size = read-only image data used by the container plus the container's writable layer
```

```
docker stats
```
 

| Module | Container Size | Min System Memory (RAM) Requirement
| --- | --- | --- |
| simple-event-detection | 84 MB | 34 MB |
| data-and-event-broker | 14 MB | 2 MB |
| knowledgebase | 772 MB | 1623 MB |
| data-ingestion | 92 MB | 43 MB |
| semantic-event-log-bridge | 80 MB | 26 MB |
| event-to-state-causality | 440 MB | 36 MB |
| explanation-generation | 151 MB | 52 MB |
| influxdb | 369 MB | 328 MB |
| --- | --- | --- |
| TOTAL | 2002 MB | 2144 MB |

## Instantiation and Execution
As mentioned, the SENSE Core is not an executable application by itself but needs to be configured according to the specific use case. This process is referred to as "instantiation". You can also use our [Demo Instantiation](https://git.ai.wu.ac.at/sense/seehub) (TODO: Replace this link, which directs to our internal seehub demo use case, to the public BIFROST Demo instantiation) of the SENSE Core to quickly start with a running demo application.

If you want to instantiate a new SENSE system from the ground up, you need to follow the steps listed below. Note that, on several occasions, it is necessary to define a name for the use case. We use USE_CASE_NAME as a placeholder, which you need to replace with the actual use case name.

### 0. Prepare the Use-Case-Specific Information
Follow our [Guide]() (TODO: add link to the guide on the necessary steps for analyzing the data, identifying relevant events, and defining appropriate explanations). During this step, the use-case-specific information will be collected and used to populate the SystemData.xlsx template. 


### 1. **Create a Use-Case-Specific Directory / GIT Repository**
We assume using a GIT repository to manage the use-case-specific instantiation of the SENSE Core. We suggest the following file structure.
```
├── sense-core
|   ├── ...
├── USE_CASE_NAME
    ├── README.md # (optional)
    ├── compose.yml # (docker compose file)
    ├── config # (configuration files for all modules)
    │   ├── data_ingestion.docker.json
    │   ├── event_to_state_causality.docker.json
    │   ├── explanation_engine.docker.json
    │   ├── knowledgebase.docker.json
    │   ├── semantic_event_log_bridge.docker.json
    │   └── simple_event_detection.docker.json
    └── infrastructure # (additional module-specific configuration and data files)
        ├── knowledgebase
        │   ├── USE_CASE_NAME_SystemData.xlsx
        ├── XLSXtoTTL.py
        ├── data
        │   ├── SENSE.ttl
        │   └── system-data.ttl
        ├── graphdb_repo_config.ttl
        └── reasoning
            └── event-reasoning.ttl
```

You can find templates for the various files in the following locations:
- **compose.yml:** Refer to the README.md files of the indiviudal modules
- **config/*:** Refer to the README.md files of the indiviudal modules
- **infrastructure/knowledgebase/USE_CASE_NAME_SystemData.xlsx:** [Template for USE_CASE_NAME_SystemData.xlsx](templates/USE_CASE_NAME_SystemData.xlsx). This file has been created in the [previous step](#1-create-a-use-case-specific-directory--git-repository).
- **infrastructure/knowledgebase/XLSXtoTTL.py:** TODO
- **infrastructure/knowledgebase/data/SENSE.ttl:** Copy from [SENSE.ttl](sense_core/knowledgebase/data/SENSE.ttl)
- **infrastructure/knowledgebase/data/system-data.ttl:** This file will be created in the [next step](#generate-system-datattl) from SystemData.xlsx via the XLSXtoTTL.py script
- **infrastructure/knowledgebase/graphdb_repo_config.ttl:** [Template for graphdb-repo-config.ttl](templates/graphdb_repo_config.ttl)
- **infrastructure/knowledgebase/reasoning/event-reasoning.ttl:** [Template for event-reasoning.ttl](templates/event-reasoning.ttl)

### 2. Generate system-data.ttl 
```
cd infrastructure/knowledgebase

# install required python packages
pip install pandas pyshacl rdflib

# create system-data.ttl from SystemData.xlsx
python3 XLSXtoTTL.py "http://example.org/USE_CASE_NAME#" USE_CASE_NAME_SystemData.xlsx ./data/system-data.ttl --shacl-path ./reasoning/event-reasoning.ttl
```

### 3. Optional: Build the Docker Images
We provide pre-built images via [registry.ai.wu.ac.at](registry.ai.wu.ac.at). However, if you would rather build the images locally, you can use the Dockerfiles/Containerfiles provided in the sense_core folder.

### 4. Run the Application
```
docker compose up
```

### 5. Request Events and Explanations

Refer to [sense_core/simple_event_detection/README.md](sense_core/simple_event_detection/README.md) for instructions on how to retrieve a list of events from your SENSE system instantiation.

Refer to [sense_core/explanation-engine/README.md](sense_core/explanation-engine/README.md) for instructions on how to retrieve explanations for events from your SENSE system instantiation. 

## Structure of the Repository
```
├── README.md
├── doc # figures etc. for the README
│   ├── ...
└── sense_core # implementations for the SENSE Core modules
    ├── config # configuration files intended for development (cf. [Configuration Files](#configuration-files))
    │   ├── data_ingestion.host.json
    │   ├── ...
    ├── data_ingestion # implementation of the data_ingestion module
    │   ├── ...
    ├── data_ingestion.Containerfile # Containerfile to build the data_ingestion module image
    ├── data_ingestion.py # entrypoint for the data_ingestion container
    ├── knowledgebase
    │   ├── data
    │   │   └── SENSE.ttl
    │   ├── ...
    ├── knowledgebase.Containerfile
    ├── knowledgebase.sh
    ├── semantic_event_log_bridge.Containerfile
    ├── shared # python code used by multiple modules
    │   ├── ...
    ├── simple_event_detection
    │   ├── ...
    ├── simple_event_detection.Containerfile
    └── simple_event_detection.py
└── .gitlab-ci.yml # gitlab CI/CD config (cf. [GitLAB CI/CD](#gitlab-cicd))
```

## Named Graphs and Namespaces
Data within the knowledgebase is organized in different named graphs according to  [SENSE Named Graph List.xlsx](https://wu.sharepoint.com/:x:/r/sites/PR-FFGSENSE/Freigegebene%20Dokumente/General/1_WorkPackages/WP4_Semantics-basedEventExplainability/4.1%20SENSE%20Semantic%20Model/SENSE%20Named%20Graph%20List.xlsx?d=w136542f1c78046dfa38a3af2cea52535&csf=1&web=1&e=01o5Rt)


## Configuration Files
Currently, we use configuration files that are specific to each module, e.g., there is a configuration file for the knowledgebase, another one for data_ingestion, etc. Eventually, we plan to merge these configuration files into one file, eliminating redundant entries/definitions.

### Configuration Files for Development
Configuration files with the file ending `host.json` are intended to be used for development only. As an example, if you want to work on the data_ingestion module, you can start all other modules within their corresponding containers and execute the data_ingestion script with its corresponding data_ingestion.host.json on your host machine.

### Configuration Files for Operation
Configuration files with the file ending `docker.json` are intended to be used when running the module in its corresponding container. They are supplied to the containers via volume mounts. As the SENSE Core is not intended to be executed, configuration files with the file ending `docker.json` should only be located in the corresponding instantiations. Eventually, we might provide templates or example `docker.json` configuration files also with the SENSE Core repository.

## GitLAB CI/CD
CI/CD build instructions for each image are defined in .gitlab-ci.yml.

## License

## References

## Contributing

## Contact