import json
import os

class NamedGraph:
    def __init__(self, uri, ttl_files):
        self.uri = uri  # First element (string)
        self.ttl_files = ttl_files  # Second element (array/list)

    def __repr__(self):
        return f"NamedGraph(uri={self.uri}, ttl_files={self.ttl_files})"

class KnowledgebaseConfiguration:
    def __init__(self, config: dict):
        self.user = config["graph-database"]["user"]
        self.password = config["graph-database"]["password"]
        self.repo_url = config["graph-database"]["repo-url"]
        self.repo_mgmt_api_url = config["graph-database"]["repo-mgmt-api-url"]
        script_path = os.path.dirname(os.path.abspath(__file__))
        self.repo_config_file = os.path.join(script_path, config["graph-database"]["repo-config-file"]) 
        self.named_graphs = []

        for named_graph in config["named-graphs"]:
            uri = named_graph["uri"]
            ttl_files = []
            for ttl_file in named_graph["ttl_files"]:
                ttl_files.append(os.path.join(script_path, ttl_file))
            self.named_graphs.append(NamedGraph(uri, ttl_files))

def load_configuration(path: str) -> KnowledgebaseConfiguration:
    with open(path) as config_file:
        json_data = json.loads(config_file.read())
        return KnowledgebaseConfiguration(json_data)
