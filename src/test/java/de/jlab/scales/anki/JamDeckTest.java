package de.jlab.scales.anki;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

public class JamDeckTest {
 
  boolean differenceFound = false;

  @Test
  public void testAll() {
    Deck<JamCard> deckC = TestUtils.writeTo(new JamDeck("Practice Chords (C-Instrument)", Note.C, false), 0.1);
    Deck<JamCard> deckGit = TestUtils.writeTo(new JamDeck("Practice Chords (Guitar)", Note.C, true), 0.1);
    Deck<JamCard> deckBb = TestUtils.writeTo(new JamDeck("Practice Chords (Bb-Instrument)", Note.Bb, false), 0.1);
    Deck<JamCard> deckEb = TestUtils.writeTo(new JamDeck("Practice Chords (Eb-Instrument)", Note.Eb, false), 0.1);

    differenceFound = false;
    writeAndDiff(deckEb);
    writeAndDiff(deckBb);
    writeAndDiff(deckGit);
    writeAndDiff(deckC);

    if (differenceFound) {
      fail("Differences in midi files found");
    }
    
  }

  private void writeAndDiff(Deck<?> deck) {
    deck.sort(0.9);
    Path dir2 = Paths.get("build/preview-2");
    writeTo(deck, dir2);

    Path dir1 = Paths.get("build/preview");
    diff(dir2, dir1, "*.midi");
  }

  private void diff(Path dir1, Path dir2, String glob) {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir1, glob)) {
      for (Path path1 : stream) {
        byte[] bytes1 = Files.readAllBytes(path1);
        Path path2 = dir2.resolve(path1.getFileName());
        byte[] bytes2 = Files.readAllBytes(path2);
        if (!Arrays.equals(bytes1, bytes2)) {
          System.out.println(String.format("***** differ: %s, %s", path1, path2));
          differenceFound = true;
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

  }

  private void writeTo(Deck<?> subdeck, Path path) {
    subdeck.writeAnki(path);
    subdeck.writeHtml(path);
    subdeck.writeJson(path);
    subdeck.writeAssets(path);

  }

  @Test
  @Ignore
  public void testC() {
    TestUtils.writeTo(new JamDeck("Practice Chords (C-Instrument)", Note.C, false), 0.01);
  }

}
