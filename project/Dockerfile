FROM maven:3.6.3-openjdk-15

ENV LANG C.UTF-8
ENV IDE_DIR /opt/online-ide

# copy files to docker container
COPY . $IDE_DIR
WORKDIR $IDE_DIR

# add maven dependencies to build in a safe way to avoid re-downloading on source-code change
RUN mvn dependency:go-offline
