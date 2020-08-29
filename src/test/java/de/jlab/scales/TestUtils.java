package de.jlab.scales;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestUtils {
  public static void assertFileContentMatches(List<String> expectedLines, Class<?> testClass, String fileName) throws IOException {
    Path dir = Paths.get("build", testClass.getSimpleName());
    Files.createDirectories(dir);
    Files.write(dir.resolve(fileName), expectedLines);
    String expected = Utils.readString(testClass.getResourceAsStream(fileName));
    String actual = expectedLines.stream().collect(joining("\n"));
    assertEquals(fileName, expected, actual);
  }

}
