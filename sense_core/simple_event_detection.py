import argparse
from simple_event_detection.configuration import load_configuration
from simple_event_detection.service import run_service


if __name__ == "__main__":
    arg_parser = argparse.ArgumentParser(prog="simple_event_detection")
    arg_parser.add_argument("-c", "--config", required=True)
    args = arg_parser.parse_args()
    config = load_configuration(args.config)

    run_service(config)
