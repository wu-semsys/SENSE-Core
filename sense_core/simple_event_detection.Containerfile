FROM docker.io/python:3.12-alpine

COPY simple_event_detection /opt/monitoring-service/simple_event_detection/
COPY shared /opt/monitoring-service/shared/
COPY simple_event_detection.py /opt/monitoring-service/

RUN pip3 install -r /opt/monitoring-service/simple_event_detection/requirements.txt

WORKDIR /opt/monitoring-service

ENTRYPOINT [ "python3", "simple_event_detection.py", "-c", "config.json" ]