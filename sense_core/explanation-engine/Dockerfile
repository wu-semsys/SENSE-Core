FROM python:3.12-slim

COPY . /opt/explanation-engine

RUN pip3 install -r /opt/explanation-engine/requirements.txt

WORKDIR /opt/explanation-engine

EXPOSE 5000

ENTRYPOINT [ "python3", "main.py"]
