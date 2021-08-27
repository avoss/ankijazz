# AnkiJazz

Code to build [AnkiJazz](https://www.ankijazz.com) website and Anki decks.

# Prerequisites

Install Anki, Java JDK 11, npm and Docker

Start Anki and create a new Profile called `build`. Then adjust Anki profile directory in `gradle.properties`. 

In `src/main/webapp` run `npm install`.

## Docker

I use ssh to get a terminal of the docker container, if you do the same, you should replace the `authorized_keys` in `docker/.ssh` directory. But you can also use Docker exec command to do so.

Generate Docker image from `docker/` directory by running `build.sh` in git bash (or linux).
Make sure the gradle `build/` directory is present. Then create a container by running `run.sh` in the docker direcotry, the Gradle `build` directory will be mounted to `/mnt` in the container.
Stop the container (`docker stop avix`) when generating media etc, because docker does not like the mounted directory to be removed e.g. when `gradlew clean` is executed.

# Steps to build website and Anki decks

## Build Media

    ./gradlew clean createRawMedia
    docker start avix
    ssh avix
    cd /mnt/anki && bash ~/anki.sh      # full data for Anki decks
    cd /mnt/preview && bash ~/anki.sh   # preview data for website
    exit
    docker stop avix

## Build Website

    ./gradlew createWebsite

## Build Anki Decks

    ./gradlew createAnkiProfileWithMedia

Then import build/anki/*.txt into Anki build profile, make sure that you use TAB as separator and allow duplicate fields.

