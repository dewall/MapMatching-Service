#!/usr/bin/env bash
set -e

docker_tag="ec-mapmatching/mapserver"

mapserver_dbname="osmdata"
mapserver_user="osmuser"
mapserver_pass="pass"
mapserver_mode="slim"

# build docker image
docker build -t="ec-mapmatching/mapserver" .

# run the docker image
docker run -d \
    -v /home/sertzu/dev/envirocar-ec4BIT/Docker/mapmatching/mapserver/data:/data \
    --name mapserver \
    ec-mapmatching/mapserver 

# wait a certain amount of time for the database to be initialized...
sleep 15

# execute the import stuff and commit the changes. 
docker exec -it mapserver /bin/bash --rcfile /root/.bashrc -ci \
    "/mnt/map/osm/import.sh /data/osm-data.osm.pbf osmdata osmuser pass /mnt/map/tools/road-types.json slim"
docker commit mapserver ec-mapmatching/mapserver
docker stop mapserver && docker rm mapserver