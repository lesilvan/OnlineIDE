FROM maven:3.6.3-openjdk-15

ENV LANG C.UTF-8
ENV IDE_DIR /opt/online-ide

# copy files to docker container
COPY . $IDE_DIR
WORKDIR $IDE_DIR
