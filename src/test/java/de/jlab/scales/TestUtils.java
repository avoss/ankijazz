package de.jlab.scales;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import de.jlab.scales.anki.Card;
import de.jlab.scales.anki.Deck;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class TestUtils {
  static boolean disabled = false;
  
  public static void assertFileContentMatches(String actualString, Class<?> testClass, String fileName) {
    assertFileContentMatches(List.of(actualString.split("\n")), testClass, fileName);
  }
  
  public static void assertFileContentMatches(List<String> actualLines, Class<?> testClass, String fileName) {
    try (InputStream inputStream = testClass.getResourceAsStream(fileName)) {
      writeActual(actualLines, testClass, fileName);
      if (disabled) return;
      String actual = sanitize(actualLines).stream().collect(joining("\n"));
      if (inputStream == null) {
        assertEquals("Resource not found: " + fileName, actual);
      }
      List<String> expectedLines = Utils.readLines(inputStream);
      String expected = sanitize(expectedLines).stream().collect(joining("\n"));
      assertEquals(fileName, expected, actual);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void assertImageMatches(BufferedImage image, Class<?> testClass, String fileName) {
    try (InputStream inputStream = testClass.getResourceAsStream(fileName)) {
      Path path = outputDir(testClass).resolve(fileName);
      File out = path.toFile();
      ImageIO.write(image,  "png", out);
      if (inputStream == null) {
        fail("Resource not found: " + fileName);
      }
      byte[] expected = inputStream.readAllBytes();
      byte[] actual = Files.readAllBytes(path);
      assertArrayEquals(expected, actual);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void assertMidiMatches(Part part, Class<?> testClass, String fileName) {
    try (InputStream inputStream = testClass.getResourceAsStream(fileName)) {
      Path path = outputDir(testClass).resolve(fileName);
      MidiFile mf = new MidiFile();
      part.perform(mf);
      mf.save(path);
      if (inputStream == null) {
        fail("Resource not found: " + fileName);
      }
      byte[] expected = inputStream.readAllBytes();
      byte[] actual = Files.readAllBytes(path);
      assertArrayEquals(expected, actual);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  private static void writeActual(List<String> actualLines, Class<?> testClass, String fileName) throws IOException {
    Path dir = outputDir(testClass);
    Files.write(dir.resolve(fileName), actualLines);
  }

  public static Path outputDir(Class<?> testClass) {
    try {
      Path dir = Paths.get("build", testClass.getPackage().getName().replace('.', '/'));
      Files.createDirectories(dir);
      return dir;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void assertFileContentMatchesInAnyOrder(List<String> actualLines, Class<?> testClass, String fileName) {
    try {
      writeActual(actualLines, testClass, fileName);
      if (disabled) return;
      List<String> expectedLines = Utils.readLines(testClass.getResourceAsStream(fileName));
      Set<String> expected = new LinkedHashSet<>(sanitize(expectedLines));
      Set<String> actual = new LinkedHashSet<>(sanitize(actualLines));
      Set<String> extra = new LinkedHashSet<>(actual);
      extra.removeAll(expected);
      Set<String> missing = new LinkedHashSet<>(expected);
      missing.removeAll(actual);
      String message = String.format("%s: extra: %s, missing: %s", fileName, extra.stream().collect(joining("\n")), missing.stream().collect(joining("\n")));
      assertEquals(message, expected, actual);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static List<String> sanitize(Collection<String> lines) {
    return lines.stream()
        .map(s -> s.replaceAll("AnkiJazz-\\w+", "AnkiJazz-XXXX"))
        .map(s -> s.replaceAll("[\r\n]", ""))
        .filter(s -> !(s.isBlank() || s.isEmpty()))
        .collect(toList());
  }
  
  public static String reviewMarker(Scale scale, KeySignature signature) {
    List<String> markers = new ArrayList<>();
    String scaleNotation = signature.toString(scale);
    if (signature.getNumberOfAccidentals() > 6) {
      markers.add("signature");
    }
    if (scaleNotation.contains("bb") || scaleNotation.contains("x")) {
      markers.add("double sharp/flat");
    }
    if (scaleNotation.contains("b") && scaleNotation.contains("#")) {
      markers.add("sharp + flat");
    }
    String rootNotation = signature.notate(scale.getRoot());
    if (rootNotation.contains("Cb") || rootNotation.contains("Fb") || rootNotation.contains("B#") || rootNotation.contains("E#")) {
      markers.add("enharmonic root");
    }
    if (containsDuplicate(scale, scaleNotation)) {
      markers.add("duplicate note");
    }
    if (markers.isEmpty()) {
      return "";
    }
    return "// " + markers.stream().collect(joining(", "));
  }

  private static boolean containsDuplicate(Scale scale, String notation) {
    String[] notes = notation.replace("#", "").replace("b", "").split(" ");
    Set<String> uniqueNotes = Arrays.stream(notes).collect(Collectors.toSet());
    return uniqueNotes.size() < scale.asList().size();
  }

  public static <T extends Card> Deck<T> writeTo(Deck<T> deck, double randomness) {
    deck.sort(randomness);
//    Path ankiDir = Paths.get("build/anki");
//    deck.writeAnki(ankiDir); 
//    deck.writeHtml(ankiDir);
//    deck.writeJson(ankiDir);
//    deck.writeAssets(ankiDir);
    
    return writePreviewOnly(deck, randomness);
  }

  public static <T extends Card> Deck<T> writePreviewOnly(Deck<T> deck, double randomness) {
    Deck<T> subdeck = deck.subdeck(25);
    subdeck.sort(randomness);
//    Path previewDir = Paths.get("build/preview");
//    subdeck.writeAnki(previewDir); 
//    subdeck.writeHtml(previewDir);
//    subdeck.writeJson(previewDir);
//    subdeck.writeAssets(previewDir);
    return subdeck;
  }

  public static KeySignature majorKeySignature(Note root) {
    List<KeySignature> list = BuiltinScaleType.Major.getKeySignatures(root).stream().collect(toList());
    assertEquals(1, list.size());
    return list.get(0);
  }

  public static KeySignature majorKeySignature(Note root, Accidental accidental) {
    return BuiltinScaleType.Major.getKeySignatures(root).stream().filter(k -> k.getAccidental() == accidental).findAny().orElseThrow();
  }

  
}
