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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.jlab.scales.anki.Deck;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;

public class TestUtils {

  public static void assertFileContentMatches(List<String> actualLines, Class<?> testClass, String fileName) {
    try (InputStream inputStream = testClass.getResourceAsStream(fileName)) {
      Path dir = Paths.get("build", testClass.getSimpleName());
      Files.createDirectories(dir);
      Files.write(dir.resolve(fileName), actualLines);
      if (inputStream == null) {
        fail("Resource not found: " + fileName);
      }
      List<String> expectedLines = Utils.readLines(inputStream);
      String actual = actualLines.stream().map(s -> s.replaceAll("\r", "")).collect(joining("\n"));
      String expected = expectedLines.stream().map(s -> s.replaceAll("\r", "")).collect(joining("\n"));
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
    return lines.stream().map(s -> s.replaceAll("AnkiJazz-\\w+.\\d+", "")).collect(toList());
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

  
  public static void checkAndWrite(Deck deck, Class<?> testClass) {
    TestUtils.assertFileContentMatches(deck.getCsv(), testClass, deck.getClass().getSimpleName());
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }
 
}
