# AnkiJazz

This is the code to build [AnkiJazz](https://www.ankijazz.com) website and [Anki decks](https://ankiweb.net/shared/decks/ankijazz).

# License

This code is licensed under the [GNU Affero General Public License](http://www.gnu.org/licenses/agpl-3.0.html)

# Build

Install Git, Java JDK 11+, npm, Docker and Anki. In your IDE you have to install [Lombok](https://projectlombok.org)

Use Git bash as your terminal window. Windows CMD will probably not work (haven't checked).

Start Anki and create a new Profile called `build`. Then adjust Anki profile directory in `anki/gradle.properties`. 

In `webapp` directory run `npm install`.

*Build Process Overview*

The anki module generates "raw media" (Lilypond source files and Midi files) into build/anki or build/preview. Then a docker container is launched, which converts "raw media" into png images and mp3 audio files, which can be consumed by Anki and the Website. The docker runs Ubuntu linux with tools like lilypond, timidity and others.

*Build Targets*

To generate the website and validate your build environment, run:

    ./gradlew clean build

If that works, your'e fine.

# About the Code
xx