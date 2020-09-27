package de.jlab.scales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.base.Charsets;

import de.jlab.scales.theory.Scale;

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

  public static String uuid() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  public static String uuid(String prefix) {
    return prefix + "-" + uuid();
  }

  public static String hash(String string) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA");
      messageDigest.update(string.getBytes(Charsets.UTF_8));
      byte [] bytes = messageDigest.digest();
      BigInteger mediator = new BigInteger(1, bytes);
      String longString = String.format("%040x", mediator);
      return "AnkiJazz-" + longString.substring(0, 10);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }

  }

  public static boolean isSymmetricalDuplicate(Scale parent, Scale mode) {
    return !parent.equals(mode) && parent.transpose(mode.getRoot()).equals(mode);
  }

}
