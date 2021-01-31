FROM maven:3.6.3-openjdk-15

ENV LANG C.UTF-8
ENV IDE_DIR /opt/online-ide
ENV JAVA_MAIN_CLASS edu.tum.ase.project.ProjectApplication

# copy files to docker container
COPY . $IDE_DIR
WORKDIR $IDE_DIR

CMD mvn exec:java -Dexec.mainClass=$JAVA_MAIN_CLASS -Dspring.profiles.active=prod
