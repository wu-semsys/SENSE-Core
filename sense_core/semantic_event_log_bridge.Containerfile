FROM docker.io/python:3.12-alpine

COPY ./semantic_event_log_bridge/ /opt/semantic-event-log_bridge/
COPY shared /opt/semantic-event-log_bridge/shared/

RUN pip3 install -r /opt/semantic-event-log_bridge/requirements.txt

WORKDIR /opt/semantic-event-log_bridge

ENTRYPOINT [ "python3", "semantic_event_log_bridge.py", "-c", "config/semantic_event_log_bridge.docker.json" ]