import argparse
from data_ingestion.service import run_service
from data_ingestion.configuration import load_configuration


if __name__ == "__main__":
    arg_parser = argparse.ArgumentParser(prog="data_ingestion")
    arg_parser.add_argument("-c", "--config", required=True)
    args = arg_parser.parse_args()
    config = load_configuration(args.config)

    run_service(config)
