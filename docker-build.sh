#!/usr/bin/env bash

build_backend_service() {
    local service_dir=$1
    local service_name=$2
    cd "$WORKDIR/$service_dir"
    echo "Building: $service_name at $WORKDIR/$service_dir"
    echo -e "\t> mvn clean compile && mvn package"
    mvn clean compile 1> $BUILD_LOG || { echo "error: Could not compile. Check log at $BUILD_LOG"; exit 1; }
    mvn package 1> $BUILD_LOG || { echo "error: Could not package. Check log at $BUILD_LOG"; exit 1; }
    echo -e "\t> docker build"
    docker build --pull -t $service_name . 1> $BUILD_LOG || { echo "error: Could not build docker image. Check log at $BUILD_LOG"; exit 1; }
    echo -e "Finished building docker image with name: $service_name"
    printf "\n"
}

build_frontend_service() {
    local service_dir=$1
    local service_name=$2

    cd $WORKDIR/$service_dir
    echo "Building: $service_name at $WORKDIR/$service_dir"
    echo -e "\t> ng build"
    cd frontend
    ng build --prod --outputPath=../src/main/resources/static &> $BUILD_LOG || { echo "error: Could not build docker angular project. Check log at $BUILD_LOG"; exit 1; }
    cd ..
    echo -e "\t> mvn compile && mvn package"
    mvn clean compile &> $BUILD_LOG || { echo "error: Could not compile. Check log at $BUILD_LOG"; exit 1; }
    mvn package &> $BUILD_LOG || { echo "error: Could not package. Check log at $BUILD_LOG"; exit 1; }
    echo -e "\t> docker build"
    docker build --pull -t $service_name . 1> $BUILD_LOG || { echo "error: Could not build docker image. Check log at $BUILD_LOG"; exit 1; }
    echo -e "Finished building docker image with name: $service_name"
    printf "\n"
}

check_openjdk_version() {
    local jdk_version=$(java --version | head -n 1 | awk '{print $2}')
    [[ $jdk_version != $OPENJDK_VERSION* ]] && echo -e "error: Need at least openjdk-version $OPENJDK_VERSION.\nVersion installed: $jdk_version" && exit 1
}

check_bins() {
    for bin in ${NEEDED_BINS[@]}; do
        which $bin &> /dev/null || { echo "error: Please install '$bin' binary."; exit 1; }
    done
}

WORKDIR=$(pwd)

NEEDED_BINS=( 'ng' 'docker' 'mvn' )

# Backend services
BACKEND_SERVICE_DIRS=( 'compiler/' 'dark-mode/' 'project/' 'discoveryserver/' 'gateway/' )
BACKEND_SERVICE_NAMES=( 'compiler-service' 'darkmode-service' 'project-service' 'discovery-service' 'gateway-service')
FRONTEND_SERVICE_DIRS=( 'ui/' )
FRONTEND_SERVICE_NAMES=( 'ui-service' )
OPENJDK_VERSION=15
BUILD_LOG="$WORKDIR/.docker-build.log"

check_bins
check_openjdk_version
touch $BUILD_LOG
echo -e "Building docker images locally. This process can take several minutes...\n"

# checking args
if [[ -n $1 ]]; then
    while [[ -n $1 ]]; do
        service=$1
        shift
        for i in ${!BACKEND_SERVICE_NAMES[@]}; do
            backend_serv=${BACKEND_SERVICE_NAMES[$i]}
            if [[ $service == $backend_serv ]]; then
                service_dir=${BACKEND_SERVICE_DIRS[$i]}
                build_backend_service $service_dir $backend_serv
            fi
        done
        for i in ${!FRONTEND_SERVICE_NAMES[@]}; do
            frontend_serv=${FRONTEND_SERVICE_NAMES[$i]}
            if [[ $service == $frontend_serv ]]; then
                service_dir=${FRONTEND_SERVICE_DIRS[$i]}
                build_frontend_service $service_dir $frontend_serv
            fi
        done
    done
else
    # build backend
    for i in ${!BACKEND_SERVICE_DIRS[@]}; do
        service_dir=${BACKEND_SERVICE_DIRS[$i]}
        service_name=${BACKEND_SERVICE_NAMES[$i]}
        build_backend_service $service_dir $service_name
    done

    # build frontend
    for i in ${!FRONTEND_SERVICE_DIRS[@]}; do
        service_dir=${FRONTEND_SERVICE_DIRS[$i]}
        service_name=${FRONTEND_SERVICE_NAMES[$i]}
        build_frontend_service $service_dir $service_name
    done
fi

echo "Removing logfile. Finished."
rm $BUILD_LOG
