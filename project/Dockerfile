FROM maven:3.6.3-openjdk-15

ENV LANG C.UTF-8
ENV IDE_DIR /opt/online-ide
ENV JAR_PATH ./target/project-0.0.1-SNAPSHOT.jar

# copy files to docker container
COPY . $IDE_DIR
WORKDIR $IDE_DIR

CMD java -jar $JAR_PATH -Dspring.profiles.active=prod
