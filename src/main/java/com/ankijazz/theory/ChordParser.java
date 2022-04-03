package com.ankijazz.theory;

import static com.ankijazz.theory.Note.C;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * parses and formats chord symbols like "Abm7". 
 * Only chords enumerated in BuiltinChordType are recognized.
 * @see BuiltinChordType
 */
public class ChordParser {
  
  public static class ParseChordException extends RuntimeException {
    private static final long serialVersionUID = -7243824667823947228L;

    public ParseChordException(String message) {
      super(message);
    }

  }
  
  
  private static final Pattern pattern = Pattern.compile("([A-G])([b#x]?)(.*)");

  private static final Map<String, ScaleType> symbolMap = 
      BuiltinChordType.stream().collect(Collectors.toMap(BuiltinChordType::getTypeName, Function.identity()));
  
  private static final Map<Scale, ScaleType> scaleMap = 
      BuiltinChordType.stream().collect(Collectors.toMap(BuiltinChordType::getPrototype, Function.identity()));
  
  public static Scale parseChord(String chord) throws ParseChordException {
    String string = chord.replace("maj7", "Δ7").replace("maj9", "Δ9");
    Matcher matcher = pattern.matcher(string);
    if (!matcher.find()) {
      throw new ParseChordException("unable to parse " + string);
    }
    Note root = Note.valueOf(matcher.group(1).toUpperCase());
    if ("b".equals(matcher.group(2))) {
      root = root.transpose(-1);
    } else if ("#".equals(matcher.group(2))) {
      root = root.transpose(1);
    } else if ("x".equals(matcher.group(2))) {
      root = root.transpose(2);
    }
    String symbol = matcher.group(3);
    ScaleType type = symbolMap.get(symbol);
    if (type == null) {
      throw new ParseChordException("could not parse " + chord);
    }
    return type.getPrototype().transpose(root);
  }
  

  public static String asChord(Scale chord) {
    return asChord(chord, null);
  }
  
  public static String asChord(Scale chord, Accidental accidental) {
    Scale transposed = chord.transpose(C);
    ScaleType type = scaleMap.get(transposed);
    if (type == null) {
      throw new NoSuchElementException("chord not found " + chord);
    }
    String rootName = (accidental != null)
        ? chord.getRoot().getName(accidental)
        : type.getKeySignatures(chord.getRoot()).iterator().next().notate(chord.getRoot());
    return rootName.concat(type.getTypeName());
  }
  
}
