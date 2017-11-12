FROM mdillon/postgis:10

MAINTAINER a.dewall@52north.org

ENV DATABASE osmdata
ENV USER osmuser
ENV PASSW pass

RUN apt-get update \
    && apt-get install -y git wget sudo \
    && apt-get install -y openjdk-8-jdk \
    && apt-get install -y python-psycopg2 python-numpy python-gdal

RUN git clone https://github.com/openstreetmap/osmosis.git /osmosis \
    && cd osmosis \
    && git checkout tags/0.43.1 \
    && ./gradlew assemble \
    && git clone https://github.com/bmwcarit/barefoot.git /barefoot \
    && mv /barefoot/map/ /mnt/ \
    && rm -r /barefoot 

RUN echo "export HOME=/root" >> /root/.bashrc && echo "export PATH=$PATH:/osmosis/package/bin" >> /root/.bashrc

ENV PGDATA /data2