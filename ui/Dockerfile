FROM alpine

ENV LANG C.UTF-8
ENV JAVA_MAIN_CLASS edu.tum.ase.ui.UiApplication
ENV WORK_DIR /usr/src/app

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

RUN yes n | npm install --global @angular/cli
RUN cd frontend && \
    yes n | npm install --save-dev @angular-devkit/build-angular
RUN cd frontend && \
        ng build --prod --outputPath=../src/main/resources/static/
RUN mvn clean compile && mvn package

CMD mvn exec:java -Dexec.mainClass=$JAVA_MAIN_CLASS -Dspring.profiles.active=prod
