package de.jlab.scales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

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

  public static String assetId(String string) {
    return assetId(string.getBytes(Charsets.UTF_8));
  }

  public static String assetId(byte[] bytes) {
    HashCode hash = Hashing.sha256().hashBytes(bytes);
    return "AnkiJazz-".concat(Integer.toHexString(hash.asInt()));
  }

  /**
   * symmetrical scales, when transposed, can result in the same scale as the original. This method returns true
   * if the mode is either the parent scale, or a mode that is not equal to the parent scale. 
   */
  public static boolean isSymmetricalDuplicate(Scale parent, Scale mode) {
    return !parent.equals(mode) && parent.transpose(mode.getRoot()).equals(mode);
  }
  
  public static <T> Iterator<T> loopIterator(Collection<T> collection) {
    return new Iterator<T>() {
      private Iterator<T> iter = collection.iterator();
     
      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public T next() {
        if (!iter.hasNext()) {
          iter = collection.iterator();
        }
        return iter.next();
      }
      
    };
  }

  public static <T> Iterator<T> randomLoopIterator(Collection<T> collection) {
    return new Iterator<T>() {
      List<T> list = new ArrayList<>(collection);

      private Iterator<T> iter = null;
     
      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public T next() {
        if (iter == null || !iter.hasNext()) {
          Collections.shuffle(list);
          iter = list.iterator();
        }
        return iter.next();
      }
      
    };
  }
  
  public static <T> List<T> repeat(int times, T ... elements) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < times; i++) {
      for (int j = 0; j < elements.length; j++) {
        result.add(elements[j]);
      }
    }
    return result;
  }

  public interface Interpolator {
    int apply(double value);
  }
  
  public static Interpolator interpolator(double inputMin, double inputMax, int outputMin, int outputMax) {
    return (input) -> {
      input = Math.max(inputMin, input);
      input = Math.min(inputMax, input);
      return (int)(outputMin + (outputMax - outputMin) * (input - inputMin) / (inputMax - inputMin));
    };
  }
  
  public static Interpolator interpolator(int outputMin, int outputMax) {
    return interpolator(0, 1, outputMin, outputMax);
  }

}
