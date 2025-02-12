#!/bin/bash

# remove the images if they already exist
docker image rm sense-core/data-ingestion:v1.0 2> /dev/null
docker image rm sense-core/event-to-state-causality:v1.0 2> /dev/null
docker image rm sense-core/explanation-interface:v1.0 2> /dev/null
docker image rm sense-core/knowledgebase:v1.0 2> /dev/null
docker image rm sense-core/semantic-event-log-bridge:v1.0 2> /dev/null
docker image rm sense-core/simple-event-detection:v1.0 2> /dev/null

# build the images
cd sense_core
docker build -t sense-core/data-ingestion:v1.0 -f data_ingestion.Containerfile .
docker build -t sense-core/event-to-state-causality:v1.0 -f event_to_state_causality.Containerfile .
docker build -t sense-core/explanation-interface:v1.0 -f explanation_interface.Containerfile .
docker build -t sense-core/knowledgebase:v1.0 -f knowledgebase.amd64.Containerfile .
docker build -t sense-core/semantic-event-log-bridge:v1.0 -f semantic_event_log_bridge.Containerfile .
docker build -t sense-core/simple-event-detection:v1.0 -f simple_event_detection.Containerfile .