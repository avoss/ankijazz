package de.jlab.scales;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.jlab.scales.anki.Deck;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;

public class TestUtils {
  static boolean disabled = false;
  
  public static void assertFileContentMatches(String actualString, Class<?> testClass, String fileName) {
    assertFileContentMatches(Collections.singletonList(actualString), testClass, fileName);
  }
  
  public static void assertFileContentMatches(List<String> actualLines, Class<?> testClass, String fileName) {
    try (InputStream inputStream = testClass.getResourceAsStream(fileName)) {
      Path dir = Paths.get("build", testClass.getSimpleName());
      Files.createDirectories(dir);
      Files.write(dir.resolve(fileName), actualLines);
      if (disabled) return;
      if (inputStream == null) {
        fail("Resource not found: " + fileName);
      }
      List<String> expectedLines = Utils.readLines(inputStream);
      String actual = sanitize(actualLines).stream().collect(joining("\n"));
      String expected = sanitize(expectedLines).stream().collect(joining("\n"));
      assertEquals(fileName, expected, actual);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void assertFileContentMatchesInAnyOrder(List<String> actualLines, Class<?> testClass, String fileName) {
    try {
      Path dir = Paths.get("build", testClass.getSimpleName());
      Files.createDirectories(dir);
      Files.write(dir.resolve(fileName), actualLines);
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
        .map(s -> s.replaceAll("\r", ""))
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
    if (markers.isEmpty()) {
      return "";
    }
    return "// " + markers.stream().collect(joining(", "));
  }

  public static void writeTo(Deck deck, double randomness) {
    Path dir = Paths.get("build/anki");
    deck.writeHtml(dir); 
    deck.sort(randomness);
    deck.writeTo(dir);
  }
 
}
