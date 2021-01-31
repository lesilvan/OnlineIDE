build_service() {
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

check_openjdk_version() {
    local jdk_version=$(java --version | head -n 1 | awk '{print $2}')
    [[ $jdk_version != $OPENJDK_VERSION* ]] && echo -e "error: Need at least openjdk-version $OPENJDK_VERSION.\nVersion installed: $jdk_version" && exit 1
}

WORKDIR=$(pwd)
SERVICE_DIRS=( 'compiler/' 'dark-mode/' 'project/' )
SERVICE_NAMES=( 'compiler-service' 'darkmode-service' 'project-service' )
OPENJDK_VERSION=15
BUILD_LOG="$WORKDIR/.docker-build.log"

check_openjdk_version
touch $BUILD_LOG
echo -e "Building docker images locally. This process can take several minutes...\n"

for i in ${!SERVICE_DIRS[@]}; do
    service_dir=${SERVICE_DIRS[$i]}
    service_name=${SERVICE_NAMES[$i]}
    build_service $service_dir $service_name
done

echo "Removing logfile. Finished."
rm $BUILD_LOG
