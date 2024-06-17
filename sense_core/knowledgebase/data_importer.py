#!/usr/bin/python
import argparse
import requests
from requests.auth import HTTPBasicAuth
from requests_toolbelt.multipart.encoder import MultipartEncoder

from configuration import load_configuration


def create_repository(config):
    # references:
    # https://graphdb.ontotext.com/documentation/10.6/configuring-a-repository.html?highlight=configuring%20repository
    # https://graphdb.ontotext.com/documentation/10.6/manage-repos-with-restapi.html
    # repo_config_file_path must end with config.ttl

    with open(config.repo_config_file, 'rb') as file:
        print(f"Creating repository with the following config: {file.read()}")
        file.seek(0)  # Reset the file pointer to the beginning of the file

        m = MultipartEncoder(
            fields={'config': ('config.ttl', file, 'text/turtle')}
        )

        # Set the headers
        headers = {'Content-Type': m.content_type}

        # Send the POST request
        auth = HTTPBasicAuth(config.user, config.password)
        response = requests.post(config.repo_mgmt_api_url, data=m, headers=headers, auth=auth)

    if response.ok:
        print(f"successfully created repository as configured in {config.repo_config_file}")
    else:
        print(f"Error while creating repository as configured in {config.repo_config_file}")
        print(response.status_code)
        print(response.text)

def upload_ttl_to_named_graph(graph_uri, ttl_file, config):
    if(graph_uri == ""): # special case: no graph.uri is specified --> upload to default graph
        url = f"{config.repo_url}/statements"
        graph_uri = "default graph" # set a value for graph_uri to get reasonable debug messages
    else:
        url = f"{config.repo_url}/rdf-graphs/service?graph={graph_uri}"

    with open(ttl_file, 'rb') as file:
        headers = {
            'Content-Type': 'application/x-turtle', 
        }
        ## change with your credentialss
        auth = HTTPBasicAuth(config.user, config.password)
        response = requests.post(url, data=file, headers=headers, auth=auth)
    if response.ok:
        print(f"{ttl_file.split('/')[-1]} successfully uploaded to graph {graph_uri} via endpoint {url}")
    else:
        print(f"Error while uploading {ttl_file.split('/')[-1]} to graph {graph_uri} via endpoint {url}")
        if(response.status_code == 405):
            print("Maybe there is a configuration error regarding your repository name or in the endpoint configuration?")
        print(response.status_code)
        print(response.text)

if __name__ == "__main__":
    arg_parser = argparse.ArgumentParser(prog="data_import")
    arg_parser.add_argument("-c", "--config", required=True)
    args = arg_parser.parse_args()

    # create the repository as defined in the repository configuration file
    # the path to the repository configuration file is defined in the knowledgebase config
    config = load_configuration(args.config)
    create_repository(config)

    # upload ttl files to their specified named graphs
    for named_graph in config.named_graphs:
        for ttl_file in named_graph.ttl_files:
            upload_ttl_to_named_graph(named_graph.uri, ttl_file, config)
