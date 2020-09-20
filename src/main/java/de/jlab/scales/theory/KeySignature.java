package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Maps;

@lombok.RequiredArgsConstructor
@lombok.Getter 
@lombok.EqualsAndHashCode
@lombok.ToString
public class KeySignature {
  private final Note majorKey;
  private final Accidental accidental;
  private final Map<Note, String> notationMap;
  private boolean suppressStaffSignature = false;
  
  public Accidental getAccidental() {
    return accidental;
  }
  
  public Note getMajorKey() {
    return majorKey;
  }
  
  public KeySignature suppressStaffSignature() {
    KeySignature keySignature = new KeySignature(Note.C, accidental, notationMap);
    keySignature.suppressStaffSignature = true;
    return keySignature;
  }
  
  /**
   * Works for Major, MelodicMinor, and Harmonic Minor. For their respective modes it also works but comes 
   * up with results that differ from their parent scale.
   * 
   * For scales with numberOfNotes != 7 a fallback keysignature (all notes flat) will be returned
   */
//  public static KeySignature fromScale(Scale scale) {
//    Analyzer analyzer = new Analyzer();
//    Analyzer.Result result = analyzer.analyze(scale);
//    return toKeySignature(result);
//  }

  public static KeySignature fromScale(Scale scale, Note notationKey, Accidental accidental) {
    if (scale.length() != CMajor.length())  {
      return fallback(scale, accidental);
    }
    Analyzer analyzer = new Analyzer();
    Analyzer.Result preferred = analyzer.analyze(scale, accidental);
    Analyzer.Result alternate = analyzer.analyze(scale, accidental.inverse());
    Analyzer.Result result = preferred;
    
    if (!preferred.getRemainingScaleNotes().isEmpty() && alternate.getRemainingScaleNotes().isEmpty()) {
      result = alternate;
    } else if (!preferred.getMajorNotesWithDoubleAccidental().isEmpty() && alternate.getMajorNotesWithDoubleAccidental().isEmpty()) {
     // result = alternate;
    }
    if (!result.getRemainingScaleNotes().isEmpty()) {
      return fallback(scale, accidental);
    }
    return toKeySignature(result, notationKey);
  }

  public static KeySignature fromMajorScale(Scale majorScale) {
    return fromScale(majorScale, majorScale.getRoot(), Accidental.fromMajorKey(majorScale.getRoot()));
  }
  
  public static KeySignature fallback(Scale scale, Accidental accidental) {
    Map<Note, String> notationMap = Stream.of(Note.values()).collect(Collectors.toMap(Function.identity(), n -> n.getName(accidental)));
    return new KeySignature(Note.C, accidental, notationMap);
  }
  
//  private static KeySignature toKeySignature(Analyzer.Result result) {
//    return toKeySignature(result, result.getRoot());
//  }
  
  private static KeySignature toKeySignature(Analyzer.Result result, Note root) {
    return new KeySignature(root, result.getAccidental(), result.getNotationMap());
  }

  public String notateKey() {
    if (suppressStaffSignature) {
      return "C";
    }
    return notate(majorKey);
  }
  
  public String notate(Note note) {
    return notationMap.get(note);
  }
  
  public List<String> notate(Scale scale) {
    return scale.asList().stream().map(this::notate).collect(toList());
  }
  
  public String toString(Scale scale) {
    return scale.asList().stream().map(this::notate).collect(joining(" "));
  }

  public int getNumberOfAccidentals() {
    return Accidental.numberOfAccidentalsInMajorKey(majorKey);
  }

}
