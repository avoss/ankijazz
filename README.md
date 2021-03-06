# AnkiJazz

This is the code to build [AnkiJazz](https://www.ankijazz.com) Website and [Anki Decks](https://ankiweb.net/shared/decks/ankijazz).

# Features

Extensive music theory library that can answer the following questions, for example:
- what scales contain a Bbm7 chord?
- what is the key signature of F Melodic Minor?
- which Pentatonic Scale is a good substitute for a Gmaj7 Arpeggio?
- what scales to use to improvise over a given chord progression (with fewest scale changes as possible)?
- a nice Midi DSL to generate backing tracks for chord progressions
- and many more!

Generate fretboard diagrams:

![Fretboard](doc/Fretboard.png)

with corresponding <a href="doc/Fretboard.mp3">Audio</a> - what you see is what you hear ;-)

Generate (randomizable but still reasonable) chord progression sheets:

![Chord Progressions](doc/ChordProgression.png)

with corresponding <a href="doc/ChordProgression.mp3">Audio</a> - again: what you see is what you hear ;-)

Generate Notation with correct Key Signatures (which is not an easy task as discussed at the bottom of [this page](https://www.ankijazz.com/scales))

![Notation](doc/Notation.png)

with corresponding <a href="doc/Notation.mp3">Audio</a> - once again: what you see is what you hear ;-)

# License

This code is licensed under the [GNU Affero General Public License](http://www.gnu.org/licenses/agpl-3.0.html)

# Build

*Prerequisites*

Install Git, Java JDK 11+, npm, Docker and Anki. In your IDE you have to install [Lombok](https://projectlombok.org)

Start Anki and create a new Profile called `build` and adjust the directory path in `anki/gradle.properties`. 

In `webapp` directory run `npm install`.

*Build Overview*

The anki subproject generates "raw media" (Lilypond source files and Midi files) into `build/anki` or `build/preview`. Then a Docker container is launched, which converts "raw media" into png images and mp3 audio files, which can be consumed by Anki and the Website. The docker runs Ubuntu Linux with tools like lilypond, timidity, lame and others. The Docker container is launched only if the file `.media-processed` is not present in the `build/anki` or `build/preview` directory (see anki/build.gradle). The `.media-processed` file is created by the Docker container after processing has finished (see [ankijazz.sh](anki/docker/ankijazz.sh))

*Build Targets*

- `build` compiles java code and runs tests, generates preview media and builds the website (which includes preview media). Does not build Anki Decks
- `buildAnkiProfile` generates media and copies them to the Anki `build` profile. This takes about 1 hour on my machine. When finished, start Anki and manually [import](https://docs.ankiweb.net/importing.html) the generated .txt files which are located in build/anki folder. Make sure that you use TAB as separator and allow duplicates.
- `processPreviewMedia` is for manual testing. When I deveop new decks I run some unit tests from IDE that generate media into build/preview directory. `processPreviewMedia` then processes whatever raw media it finds in `build/preview` directory. After that I open build/preview/*.html files in a browser to check whether the generated media look right. 

# About the Code

TBD.