FROM docker.io/python:3.12-alpine

COPY data_ingestion /opt/data-ingestion/data_ingestion/
COPY shared /opt/data-ingestion/shared/
COPY data_ingestion.py /opt/data-ingestion/

RUN pip3 install -r /opt/data-ingestion/data_ingestion/requirements.txt

WORKDIR /opt/data-ingestion

ENTRYPOINT [ "python3", "data_ingestion.py", "-c", "config.json" ]