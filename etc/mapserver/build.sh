#!/usr/bin/env bash
set -e

osm_dl_link="http://download.geofabrik.de/europe/germany/nordrhein-westfalen-latest.osm.pbf"

docker_tag="ec-mapmatching/mapserver"

mapserver_dbname="osmdata"
mapserver_user="osmuser"
mapserver_pass="pass"
mapserver_mode="slim"

scriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${scriptDir}" #&& mkdir data

# build docker image
docker build -t="ec-mapmatching/mapserver" .

# run the docker image
docker run -d \
    -v $scriptDir/data:/data \
    --name mapserver \
    ec-mapmatching/mapserver 

# wait a certain amount of time for the database to be initialized...
#sleep 15

# execute the import stuff and commit the changes. 
docker exec -it mapserver /bin/bash --rcfile /root/.bashrc -ci \
    "wget -O /data/osm-data.osm.pbf $osm_dl_link && /mnt/map/osm/import.sh /data/osm-data.osm.pbf $mapserver_dbname $mapserver_user $mapserver_pass /mnt/map/tools/road-types.json $mapserver_mode"
docker commit mapserver ec-mapmatching/mapserver
docker stop mapserver && docker rm mapserver