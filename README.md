# ASE Practical Exercise - Group 1-2 - WS20

## Code Freeze

*This project has freezed on 8th of febuary, 15:21.*

## Docker: Local Setup

First compile, package and then build all local docker images with the provided script. Keep it mind that when you change sourcecode, you have to rebuild the images!

```
$ ./docker-build.sh
```

## Docker: Local Runtime

Source environment variables used for setting up database configuration:

```
$ set -a
$ source .env.dev
$ source .env.ci
```

Use docker-compose to setup a microservice mesh with an according project database:

```
$ docker-compose up
```

To gracefully remove artifacts and shutdown the environment, please use:

```
$ docker-compose down
```

## Docker: Production Runtime (via gitlab-runner)

*This is for use on the cloud server.*

The runner dependencies are as follows:
```
    - openjdk >= 15.0
    - maven >= 3.5
    - docker >= 18.0
```

All building logic resides in `.gilab-ci.yml`. The built jars then get copied to docker images where they then can be run as containers.

All docker images reside in this project's local [container registry](https://gitlab.lrz.de/ase20-group1-2/ase20-practical-exercise/container_registry). To use it, you have to login the runner with your credentials (further info [here](https://docs.gitlab.com/ee/user/packages/container_registry/#authenticate-by-using-gitlab-cicd)).

```
$ docker login docker login -u <username> -p <access_token> gitlab.lrz.de:5005
```

In production, the runner copies `docker-compose.prod.yml` to the GCP server and issues a simple:

```
$ docker-compose up
```

This will pull all images from the local gitlab registry and spin up all needed containers.

