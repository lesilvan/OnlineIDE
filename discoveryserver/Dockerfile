FROM maven:3.6.3-openjdk-15

ENV LANG C.UTF-8
ENV IDE_DIR /opt/service
ENV JAVA_MAIN_CLASS edu.tum.ase.discoveryserver.DiscoveryserverApplication

# copy files to docker container
COPY . $IDE_DIR
WORKDIR $IDE_DIR

CMD mvn exec:java -Dexec.mainClass=$JAVA_MAIN_CLASS -Dspring.profiles.active=prod
