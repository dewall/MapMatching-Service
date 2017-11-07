FROM java:8

VOLUME  /tmp
ADD map-data/ /app/map-data/    
ADD mmservice-0.1.jar /app/app.jar

RUN bash -c 'touch /app/app.jar'
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
