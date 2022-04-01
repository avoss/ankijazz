# AnkiJazz

This is the code to build [AnkiJazz](https://www.ankijazz.com) website and [Anki decks](https://ankiweb.net/shared/decks/ankijazz).

# License

This code is licensed under the [GNU Affero General Public License](http://www.gnu.org/licenses/agpl-3.0.html)

# Prerequisites
Install Anki, Java JDK 11, npm and Docker. In your IDE you have to install [Lombok](https://projectlombok.org)

Start Anki and create a new Profile called `build`. Then adjust Anki profile directory in `gradle.properties`. 

In `src/main/webapp` run `npm install`.

The java code generates Lilypond source files and Midi files into build/anki or build/preview. Then a docker container is launched, which runs lilypond, timidity and other tools to generate notation images from \*.ly and MP3 audio files from \*.midi.

# Build

To build the full website, use

    ./gradlew clean createWebsite

You can also generate just the preview media which is part of the website using this command:

	./gradlew clean generateAndProcessPreviewMedia

This is also a good test to see, whether your build setup is correct.

    ./gradlew clean createAnkiProfileWithMedia

This will take an hour or so! Then start Anki and manually [import](https://docs.ankiweb.net/importing.html) the generated .txt files which are located in build/anki folder. Make sure that you use TAB as separator and allow duplicates.

When I deveop new decks and want to do manual testing, then I run some unit tests from IDE that generate media into build/preview directory. Then I run

	./gradlew processPreviewMedia
	
which processes whatever raw media it finds in build/preview. After that I open build/preview/*.html files in a browser to check whether the generated media look right. Note that at least some of my decks use random UUIDs, so the preview folder will grow and grow until you clean it up.

# About the Code
xx