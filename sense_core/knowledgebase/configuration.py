import json
import os

class NamedGraph:
    def __init__(self, uri, ttl_files, reupload):
        self.uri = uri
        self.ttl_files = ttl_files
        self.reupload = reupload 

    def __repr__(self):
        return f"NamedGraph(uri={self.uri}, ttl_files={self.ttl_files})"

class KnowledgebaseConfiguration:
    def __init__(self, config: dict):
        self.user = config["graph-database"]["user"]
        self.password = config["graph-database"]["password"]
        self.repo_name = config["graph-database"]["repo-name"]
        self.repo_url = config["graph-database"]["repo-url"]
        self.repo_mgmt_api_url = config["graph-database"]["repo-mgmt-api-url"]
        self.repo_name_final = config["graph-database"].get("repo-name-final",None)
        self.repo_recreate = config["graph-database"]["repo-recreate"] # if repo_recreate==true, the script will delete the repository and recreate it as defined in the repo_config_file
        script_path = os.path.dirname(os.path.abspath(__file__))
        self.repo_config_file = os.path.join(script_path, config["graph-database"]["repo-config-file"]) 
        self.named_graphs = []

        for named_graph in config["named-graphs"]:
            uri = named_graph["uri"]
            ttl_files = []
            for ttl_file in named_graph["ttl_files"]:
                ttl_files.append(os.path.join(script_path, ttl_file))
            reupload = named_graph["reupload"] # if reupload==true, the script will delete the corresponding graph and reupload the data from the corresponding ttl files
            self.named_graphs.append(NamedGraph(uri, ttl_files, reupload))

def load_configuration(path: str) -> KnowledgebaseConfiguration:
    with open(path) as config_file:
        json_data = json.loads(config_file.read())
        return KnowledgebaseConfiguration(json_data)
