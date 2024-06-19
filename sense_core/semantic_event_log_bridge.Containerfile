FROM docker.io/python:3.12-alpine

COPY ./semantic_event_log_bridge/ /opt/semantic-event-log_bridge/
COPY ./config/semantic_event_log_bridge.docker.json /opt/semantic-event-log_bridge/config/semantic_event_log_bridge.docker.json

RUN pip3 install -r /opt/semantic-event-log_bridge/requirements.txt

WORKDIR /opt/semantic-event-log_bridge

ENTRYPOINT [ "python3", "semantic_event_log_bridge.py", "-c", "config/semantic_event_log_bridge.docker.json" ]