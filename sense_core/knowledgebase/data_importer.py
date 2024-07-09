#!/usr/bin/python
import argparse
import requests
from requests.auth import HTTPBasicAuth
from requests_toolbelt.multipart.encoder import MultipartEncoder

from configuration import load_configuration

def check_repository_exists(repo_mgmt_api_url, repo_name):
    response = requests.get(config.repo_mgmt_api_url)
    if response.ok:
        # Parse the JSON response
        repositories = response.json()
        # Extract and print the list of repository IDs
        repo_list = [repo['id'] for repo in repositories]

        if(repo_name in repo_list):
            return True
        else:
            return False
    else:
        print(f"Error while queryiing existing repositories from endpoint " + config.repo_mgmt_api_url)

def delete_repository(repo_mgmt_api_url, repo_name, auth=None):
    """
    Delete a repository in GraphDB.
    
    :param graphdb_url: The base URL of the GraphDB instance.
    :param repository_name: The name of the repository to delete.
    :param auth: Optional tuple for basic HTTP authentication: ('username', 'password').
    :return: None
    """
    print(f"Deleting repository: {repo_name}")

    endpoint = f"{repo_mgmt_api_url}/{repo_name}"

    # Headers
    headers = {
        "Accept": "application/json"
    }

    # Send DELETE request
    response = requests.delete(endpoint, headers=headers, auth=auth)

    # Check the response
    if response.ok:
        print(f"Repository '{repo_name}' deleted successfully.")
    elif response.status_code == 404:
        print(f"Repository '{repo_name}' not found.")
    else:
        print(f"Failed to delete repository '{repo_name}'. Status code: {response.status_code}")
        print(f"Response: {response.text}")


def check_named_graph_exists(repo_url, graph_iri, auth=None):
    """
    Check if a named graph exists in GraphDB.
    
    :param repo_url: The URL of the GraphDB SPARQL endpoint.
    :param graph_iri: The IRI of the named graph to check.
    :param auth: Optional tuple for basic HTTP authentication: ('username', 'password').
    :return: True if the named graph exists, False otherwise.
    """
    # SPARQL ASK query to check if the named graph exists
    query = f"""
    ASK WHERE {{
      GRAPH <{graph_iri}> {{ ?s ?p ?o }}
    }}
    """
        
    # Headers
    headers = {
        "Accept": "application/sparql-results+json"
    }
    
    # Send POST request to the SPARQL endpoint
    data = {"query": query}
    response = requests.post(url=repo_url, data=data, headers=headers)
    
    # Check the response
    if response.ok:
        result = response.json()
        return result['boolean']
    else:
        print(f"Failed to check named graph '{graph_iri}'. Status code: {response.status_code}")
        print(f"Response: {response.text}")
        if(response.status_code == 405):
            print("Maybe there is a configuration error regarding your repository name or the endpoint configuration?")
        return False

def delete_named_graph(repo_url, graph_iri, auth=None):
    """
    Delete a named graph in GraphDB.
    
    :param repo_url: The URL of the GraphDB repo.
    :param graph_iri: The IRI of the named graph to delete.
    :param auth: Optional tuple for basic HTTP authentication: ('username', 'password').
    :return: None
    """
    print(f"Deleting named graph: {graph_iri}")

    params = {"graph": graph_iri}
    response = requests.delete(
        f"{repo_url}/rdf-graphs/service", params=params
    )
    
    # Check the response
    if response.ok:
        print(f"Named graph '{graph_iri}' deleted successfully.")
    else:
        print(f"Failed to delete named graph '{graph_iri}'. Status code: {response.status_code}")
        print(f"Response: {response.text}")

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
            print("Maybe there is a configuration error regarding your repository name or the endpoint configuration?")
        print(response.status_code)
        print(response.text)

if __name__ == "__main__":
    arg_parser = argparse.ArgumentParser(prog="data_import")
    arg_parser.add_argument("-c", "--config", required=True)
    args = arg_parser.parse_args()

    # the path to the repository configuration file is defined in the knowledgebase config
    config = load_configuration(args.config)

    # check if the repository already exists, if not
    # create the repository as defined in the repository configuration file
    if (check_repository_exists(config.repo_mgmt_api_url, config.repo_name)):
        print(f"repository {config.repo_name} already exists")
        if(config.repo_recreate):
            print(f"deleting and recreating the repo according to the given config")
            delete_repository(config.repo_mgmt_api_url, config.repo_name)
            create_repository(config)
        else:
            pass # the repo exists and should not be recreated, i.e., there is nothing to be done
    else: 
        create_repository(config)

    # upload ttl files to their specified named graphs
    for named_graph in config.named_graphs:
        if check_named_graph_exists(config.repo_url, named_graph.uri):
            print(f"named graph {named_graph.uri} already exists")
            if named_graph.reupload:
                print(f"deleting and recreating the named graph with data from the specified ttl files")
                delete_named_graph(config.repo_url, named_graph.uri)
                for ttl_file in named_graph.ttl_files:
                    upload_ttl_to_named_graph(named_graph.uri, ttl_file, config)
            else:
                pass # the named graph exists and should not be reuploaded, i.e., there is nothing to be done
        else:
            for ttl_file in named_graph.ttl_files:
                upload_ttl_to_named_graph(named_graph.uri, ttl_file, config)
        # check if reupload is set
