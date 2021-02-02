FROM alpine

ENV LANG C.UTF-8
ENV WORK_DIR /usr/src/app
ENV JAR_PATH ./target/ui-0.0.1-SNAPSHOT.jar

# copy files to docker container
COPY . $WORK_DIR
# WORKDIR must be run before RUN cmds
WORKDIR $WORK_DIR

# add testing repo for openjdk15
RUN export TESTING_REPO="http://$(head -n 1 /etc/apk/repositories | awk 'BEGIN{FS="/"} {print $3}')/alpine/edge/testing" && \
    apk add --no-cache -X $TESTING_REPO openjdk15

# run update, upgrade and install packages
RUN apk update && apk upgrade && \
    apk add --no-cache openjdk15 maven nodejs npm

CMD java -jar $JAR_PATH -Dspring.profiles.active=prod
