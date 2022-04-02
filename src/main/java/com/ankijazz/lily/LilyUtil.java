package com.ankijazz.lily;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import com.ankijazz.Utils;
import com.ankijazz.theory.KeySignature;
import com.ankijazz.theory.Note;

public abstract class LilyUtil {
  
  public abstract String toLily();
  
  public static String readTemplate(Class<?> clazz, String templateName) {
    try (InputStream input = clazz.getResourceAsStream(templateName)) {
      return Utils.readString(input);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  public static String toLilyNote(KeySignature keySignature, Note note) {
    return toLilyNote(keySignature.notate(note));
  }
  // TODO use Accidental instead of strings
  private static String toLilyNote(String notatedNote) {
    return notatedNote
        .replace("b", "f")
        .replace("#", "s")
        .replace("x", "ss")
        .toLowerCase();
  }

}
