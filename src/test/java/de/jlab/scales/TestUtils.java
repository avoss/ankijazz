package de.jlab.scales;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {
  public static void assertFileContentMatches(List<String> expectedLines, Class<?> testClass, String fileName) throws IOException {
    Path dir = Paths.get("build", testClass.getSimpleName());
    Files.createDirectories(dir);
    Files.write(dir.resolve(fileName), expectedLines.stream().map(String::trim).collect(toList()));
    String expected = Utils.read(testClass.getResourceAsStream(fileName)).trim();
    String actual = expectedLines.stream().collect(Collectors.joining("\n")).trim();
    assertEquals(fileName, expected, actual);
  }

}
