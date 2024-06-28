FROM docker.io/ontotext/graphdb:10.4.0

ENV GDB_JAVA_OPTS "-Xmx3g -Xms3g \
        -Dgraphdb.home=/opt/graphdb/home \
        -Dgraphdb.workbench.importDirectory=/opt/graphdb/import \
        -Dgraphdb.workbench.cors.enable=true \
        -Denable-context-index=true \
        -Dentity-pool-implementation=transactional \
        -Dhealth.max.query.time.seconds=60 \
        -Dgraphdb.append.request.id.headers=true \
        -Dreuse.vars.in.subselects=true"

# Use apk add for installing packages
RUN apk add --no-cache python3 py3-pip iptables

# use a python script that, when the container starts, creates the repository, imports SENSE.ttl, and then imports the system data
COPY ./knowledgebase/data_importer.py /opt/knowledgebase/data_importer.py
COPY ./knowledgebase/configuration.py /opt/knowledgebase/configuration.py
COPY ./knowledgebase/requirements.txt /opt/knowledgebase/requirements.txt 
RUN pip install -r /opt/knowledgebase/requirements.txt

COPY ./knowledgebase.sh /opt/knowledgebase/knowledgebase.sh
RUN chmod a+x /opt/knowledgebase/knowledgebase.sh

COPY ./knowledgebase/data/SENSE.ttl /opt/knowledgebase/SENSE.ttl

ENTRYPOINT ["/opt/knowledgebase/knowledgebase.sh"]