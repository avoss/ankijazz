package de.jlab.scales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Utils {
  private Utils() {
  }

  public static String readString(InputStream input) {
    return readLines(input).stream().collect(Collectors.joining("\n"));
  }

  public static List<String> readLines(InputStream input) {
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
      return buffer.lines().collect(Collectors.toList());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  static int counter = 1;
  public static String uuid() {
    //return UUID.randomUUID().toString().substring(0, 8);
    return String.format("%04d", counter++);
    
  }

  public static String uuid(String prefix) {
    return prefix + "-" + uuid();
  }

}
