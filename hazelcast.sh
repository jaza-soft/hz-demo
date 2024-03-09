#!/bin/bash

docker stop hazelcast

docker rm hazelcast

docker run -d --name hazelcast \
    --network hz-network \
    -e HZ_NETWORK_PUBLICADDRESS=192.168.1.110:5701 \
    -p 5701:5701 \
    -e JAVA_OPTS="-Dhazelcast.config=/opt/hazelcast/config_ext/hazelcast.xml" \
    -v .:/opt/hazelcast/config_ext \
    hazelcast/hazelcast:5.3