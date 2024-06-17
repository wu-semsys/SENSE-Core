#!/bin/bash
echo "This container runs GRAPHDB"
echo "When graphdb has been started, it creates a repository, and imports the SENSE Semantic Model and the System Data into the corresponding named graphs"

set -x #echo on

touch /opt/graphdb/graphdb.log

# close port 7200 for external access to avoid other containers to connect if the data is not yet loaded
# iptables -A INPUT -p tcp --dport 7200 -j DROP
iptables -A INPUT -p tcp -s 127.0.0.1 --dport 7200 -j ACCEPT
iptables -A INPUT -p tcp --dport 7200 -j DROP

# in a background task: wait for graphdb to start and then execute the pyhton script for data import
(
    # wait until graphdb is running properly
    grep -q "Started GraphDB in workbench mode" <(tail -f /opt/graphdb/graphdb.log)

    # import ttl files describing the system
    python3 /opt/knowledgebase/data_importer.py --config /opt/knowledgebase/config.json |& tee -a /opt/knowledgebase/data_importer.log

    # drop all firewall rules (which now allows traffic to port 7200)
    iptables -F
    echo "GRAPHDB started"
) &

# start graphdb
/opt/graphdb/dist/bin/graphdb |& tee -a /opt/graphdb/graphdb.log

