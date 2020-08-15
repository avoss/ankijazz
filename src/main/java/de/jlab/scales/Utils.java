package de.jlab.scales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Utils {
  private Utils() {
  }

  public static String read(InputStream input) {
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
      return buffer.lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  public static String uuid() {
    return UUID.randomUUID().toString().substring(0, 8);
    
  }

  public static String uuid(String prefix) {
    return prefix + "-" + uuid();
  }

}
