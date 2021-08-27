# AnkiJazz

Code to build the [AnkiJazz](https://www.ankijazz.com) website and the Anki decks.

# Prerequisites

Java JDK 11, npm and Docker installed. Generate Docker image from `docker/` directory and create a container - mine is called `avix`. Make sure, the Gradle `build` directory is mounted to `/mnt` in the container.

# Steps to build website and Anki decks

## Sync Git

    git checkout release
    git merge develop
    git push

## Build Media

    ./gradlew clean createRawMedia
    docker start avix
    ssh avix
    (cd /mnt/anki && bash ~/anki.sh) && (cd /mnt/preview && bash ~/anki.sh)
    exit
    docker stop avix

## Build Website

    ./gradlew createWebsite

## Build Decks

    ./gradlew createAnkiProfileWithMedia
    import *.txt into Anki build profile

