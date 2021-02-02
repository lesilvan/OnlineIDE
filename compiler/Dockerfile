FROM alpine

ENV LANG C.UTF-8
ENV IDE_DIR /opt/service
ENV JAR_PATH ./target/compiler-0.0.1-SNAPSHOT.jar

# copy files to docker container
COPY . $IDE_DIR
WORKDIR $IDE_DIR

# add testing repo for openjdk15
RUN export TESTING_REPO="http://$(head -n 1 /etc/apk/repositories | awk 'BEGIN{FS="/"} {print $3}')/alpine/edge/testing" && \
    apk add --no-cache -X $TESTING_REPO openjdk15

# run update, upgrade and install packages
RUN apk update && apk upgrade && \
    apk add --no-cache openjdk15 maven gcc musl-dev

CMD java -jar $JAR_PATH -Dspring.profiles.active=prod
