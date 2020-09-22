package de.jlab.scales;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;

public class TestUtils {

  public static void assertFileContentMatches(List<String> actualLines, Class<?> testClass, String fileName) {
    try {
      Path dir = Paths.get("build", testClass.getSimpleName());
      Files.createDirectories(dir);
      Files.write(dir.resolve(fileName), actualLines);
      String expected = Utils.readString(testClass.getResourceAsStream(fileName));
      String actual = actualLines.stream().collect(joining("\n"));
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
    String scaleNotation = signature.toString(scale);
    String rootNotation = signature.notate(scale.getRoot());
    return scaleNotation.contains("bb") 
           || scaleNotation.contains("x")
           || rootNotation.contains("Cb")
           || rootNotation.contains("Fb")
           || rootNotation.contains("B#")
           || rootNotation.contains("E#") ? " (*)" : "";
  }

}
