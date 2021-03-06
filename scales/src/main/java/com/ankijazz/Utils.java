package com.ankijazz;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.ankijazz.midi.MidiFile;
import com.ankijazz.midi.Part;
import com.ankijazz.theory.KeySignature;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleType;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public final class Utils {
  private static final String ANKI_JAZZ = "AnkiJazz-";

  private Utils() {
  }

  public static <T> T getLast(List<? extends T> list) {
    return list.get(list.size() - 1);
  }
  
  public static <T> T getFirst(List<? extends T> list) {
    return list.get(0);
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
    return ANKI_JAZZ.concat(UUID.randomUUID().toString().substring(0, 8));
  }

  public static String assetId(String string) {
    return assetId(string.getBytes(Charsets.UTF_8));
  }

  public static String assetId(byte[] bytes) {
    HashCode hash = Hashing.sha256().hashBytes(bytes);
    return ANKI_JAZZ.concat(Integer.toHexString(hash.asInt()));
  }

  public static String assetId(byte[] bytes1, byte[] bytes2) {
    byte[] allBytes = new byte[bytes1.length + bytes2.length];
    System.arraycopy(bytes1, 0, allBytes, 0, bytes1.length);
    System.arraycopy(bytes2, 0, allBytes, bytes1.length, bytes2.length);
    return Utils.assetId(allBytes);
  }
  
  /**
   * symmetrical scales, when transposed, can result in the same scale as the original. This method returns true
   * if the mode is either the parent scale, or a mode that is not equal to the parent scale. 
   */
  public static boolean isSymmetricalDuplicate(Scale parent, Scale mode) {
    return !parent.equals(mode) && parent.transpose(mode.getRoot()).equals(mode);
  }
  
  public static <T> Iterator<T> loopIterator(Collection<? extends T> collection) {
    return new Iterator<T>() {
      private Iterator<? extends T> iter = collection.iterator();
     
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

  public static <T> Iterator<T> randomLoopIterator(Collection<? extends T> collection) {
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

  @FunctionalInterface
  public static interface LoopIteratorFactory {
    <T> Iterator<T> iterator(Collection<? extends T> collection);
  }

  public static LoopIteratorFactory randomLoopIteratorFactory() {
    return new LoopIteratorFactory() {

      @Override
      public <T> Iterator<T> iterator(Collection<? extends T> collection) {
        return randomLoopIterator(collection);
      }
      
    };
  }

  public static LoopIteratorFactory fixedLoopIteratorFactory() {
    return new LoopIteratorFactory() {

      @Override
      public <T> Iterator<T> iterator(Collection<? extends T> collection) {
        return loopIterator(collection);
      }
      
    };
  }

  public static <T> List<T> take(Iterator<T> iterator, int count) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      result.add(iterator.next());
    }
    return result;
  }

  public static boolean linux() {
    return new File("/usr/bin/bash").exists();
  }
  
  public static String chordName(ScaleType chord, Note root) {
    KeySignature keySignature = chord.getKeySignatures(root).iterator().next();
    return keySignature.notate(root).concat(chord.getTypeName());
  }

  public static void writeImage(Path path, BufferedImage image) {
    try {
      File out = path.toFile();
      Files.createDirectories(path.getParent());
      ImageIO.write(image,  "png", out);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void writeMidi(Path path, Part part) {
    MidiFile mf = new MidiFile();
    part.perform(mf);
    mf.save(path);
  }
  
}
