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

Ensure that in TestUtils.java, writeTo() and writePreviewOnly() are not commented out.

    ./gradlew clean build
    docker start avix
    ssh avix
    (cd /mnt/anki && bash ~/anki.sh) && (cd /mnt/preview && bash ~/anki.sh)
    exit
    docker stop avix

## Build Website

    ./gradlew createWebsitePreview
    QA: run ng serve check website, especially carousels and previews
    ng build
    upload new website to ankijazz.com

## Build Decks

    ./gradlew createAnkiProfileWithMedia
    import txt into Anki build profile
    export decks from Anki build profile to /D/Backup/Ankijazz
    import decks into Anki ankijazz profile
    QA: browse decks in Anki 
    synchronize ankijazz profile
    re-share decks (with same name!)

