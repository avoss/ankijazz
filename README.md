# AnkiJazz

This is the code to build [AnkiJazz](https://www.ankijazz.com) website and [Anki decks](https://ankiweb.net/shared/decks/ankijazz).

# Features

# License

This code is licensed under the [GNU Affero General Public License](http://www.gnu.org/licenses/agpl-3.0.html)

# Build

Install Git, Java JDK 11+, npm, Docker and Anki. In your IDE you have to install [Lombok](https://projectlombok.org)

Use Git bash as your terminal window. Windows CMD will probably not work (haven't checked).

Start Anki and create a new Profile called `build`. Then adjust Anki profile directory in `anki/gradle.properties`. 

In `webapp` directory run `npm install`.

*Build Process Overview*

The anki module generates "raw media" (Lilypond source files and Midi files) into build/anki or build/preview. Then a docker container is launched, which converts "raw media" into png images and mp3 audio files, which can be consumed by Anki and the Website. The docker runs Ubuntu linux with tools like lilypond, timidity, lame and others. The Docker container is launched only if the file `.media-processed` is not present in the anki or preview directory (see anki/build.gradle).

*Build Targets*

- `build` compiles java code and runs tests, generates preview media (which is included into the website) and builds the website. Does not build Anki Decks
- `buildAnkiProfile` generates media and copies them to the Anki `build` profile. This takes about 1 hour on my machine. When finished, start Anki and manually [import](https://docs.ankiweb.net/importing.html) the generated .txt files which are located in build/anki folder. Make sure that you use TAB as separator and allow duplicates.
- `processPreviewMedia` for manual testing. When I deveop new decks I run some unit tests from IDE that generate media into build/preview directory. `processPreviewMedia` then processes whatever raw media it finds in build/preview. After that I open build/preview/*.html files in a browser to check whether the generated media look right. 

# About the Code
xx