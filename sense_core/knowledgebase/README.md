# Knowledgebase

This service is responsible for providing a semantic data storage to the SENSE system. It initializes the semantic data storage (GraphDB) with a repository, named graphs, and ttl files as defined in the configuration.

## Configuration

The Knowledgebase service can be configured via a json file. The user provides the path to this configuration file when invoking the service. This section details the configuration structure.

### Graph Database Configuration (`graph-database`)

This section defines the connection to the graph database. It is used by the script that initializes the repository and uploads ttl files to the corresponding named graphs.

`user`: username for accessing the graph database

`password`: password for accessing the graph database

`repo-name`: name of the repository to create and/or load data into

`repo-url`: url to the repository to create and/or load data into

`repo-mgmt-api-url`: url for managing graph database repositories [](https://rdf4j.org/documentation/reference/rest-api/)

`repo-config-file`: file used for repository configuration

`repo-recreate`: if true, the repo will be deleted and recrated upon startup, even if the container already existed. if false, data in the already existing container will be preserved

### Named Graphs Configuration and Initial Data (`named-graphs`)

This section defines which ttl files shall be uploaded to which named graphs upon startup

`uri`: uri of the named graph

`ttl_files`: a list of ttl files that shall be loaded

`reupload`:  if true, the named graph will be deleted and the data will be reloaded upon startup, even if the container already existed. if false, data for this named graph in the already existing container will be preserved

### Examples

An example for replaying data from historical data from an InfluxDB.

```json
{
    "graph-database": {
        "user": "username",
        "password": "password",
        "repo-name": "sense",
        "repo-url": "http://localhost:7200/repositories/sense",
        "repo-mgmt-api-url": "http://localhost:7200/rest/repositories",
        "repo-config-file": "./graphdb-repo-config.ttl",
        "repo-recreate": false
    },
    "named-graphs": [
        {
            "uri": "http://w3id.org/explainability/graph/ontology",
            "ttl_files": [
                "./data/SENSE.ttl"
            ],
            "reupload": true
        }
    ]
}
