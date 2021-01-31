# ASE Practical Exercise - Group 1-2 - WS20

## Docker: Local Setup

First compile, package and then build all local docker images with the provided script. Please make sure that all spring-boot services contain an `application-prod.yaml` file, where they define port `:8080` as their communication port, because docker containers are built with production-environment in mind, but you can use the mesh also locally for dev purposes. Keep it mind that when you change sourcecode, you have to rebuild the images!

```
$ ./docker-build.sh
```

## Docker: Local Runtime

Source environment variables used for setting up database configuration:

```
$ source .env.prod
```

Use docker-compose to setup a microservice mesh with an according project database:

```
$ docker-compose up
```

## Docker: Production Runtime

*This is for use on the cloud server.*

All docker images reside in this project's local [container registry](https://gitlab.lrz.de/ase20-group1-2/ase20-practical-exercise/container_registry). To use it, you have to login with your credentials (further info [here](https://docs.gitlab.com/ee/user/packages/container_registry/#authenticate-by-using-gitlab-cicd)).

```
$ docker login docker login -u <username> -p <access_token> gitlab.lrz.de:5005
```

Then you can simply use:

```
$ docker-compose up
```

This will pull all images from the local registry and spin up containers according to this project's `docker-compose.yml`.


## Run app without docker

How to run this project

Start UI only (without microservice integration)
1. Change current path into subfolder UI/FRONTEND: `cd ui/frontend`
2. Run `ng serve`
3. Navigate to `http://localhost:4200/home` in the browser

Start UI (integrated in Microservice)
1. Change current path into subfolder UI/FRONTEND: `cd ui/frontend`
2. Build static files with `ng build --prod --outputPath=../src/main/resources/static/`
3. Start the spring boot UI application (main in UiApplication)
4. Navigate to `http://localhost:8080/home` in browser

Start other microservices
- Project (runs on port 8081)
