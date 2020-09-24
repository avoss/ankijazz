package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.jlab.scales.theory.Analyzer.Result;

/**
 * TODO: 
 * - if (enharmonic root or double accidental or missing scale notes) { return fallback }
 * - fallback should have badness
 * 
 * @author andreas
 *
 */
@lombok.RequiredArgsConstructor
@lombok.Getter 
@lombok.EqualsAndHashCode
@lombok.ToString
public class KeySignature {
  private final Note notationKey;
  private final Accidental accidental;
  private final Map<Note, String> notationMap;
  private boolean suppressStaffSignature = false;
  private static boolean strict = true;
  
  public KeySignature suppressStaffSignature() {
    KeySignature keySignature = new KeySignature(Note.C, accidental, notationMap);
    keySignature.suppressStaffSignature = true;
    return keySignature;
  }

//  public static KeySignature fromScale(Scale scale, Accidental preferredAccidental) {
//    if (scale.length() != CMajor.length())  {
//      return fallback(scale, preferredAccidental);
//    }
//    Analyzer analyzer = new Analyzer();
//    Analyzer.Result preferred = analyzer.analyze(scale, preferredAccidental);
//    Analyzer.Result alternate = analyzer.analyze(scale, preferredAccidental.inverse());
//    if (preferred.getBadness() > alternate.getBadness()) {
//      return toKeySignature(alternate);
//    }
//    return toKeySignature(preferred);
//  }

  public static KeySignature fromScale(Scale scale, Note notationKey, Accidental accidental) {
    if (scale.length() != CMajor.length())  {
      return fallback(scale, notationKey, accidental);
    }
    Analyzer analyzer = new Analyzer();
    Result preferred = analyzer.analyze(scale, accidental);
    Result alternate = analyzer.analyze(scale, accidental.inverse());
    Result result = chooseBetterOne(preferred, alternate);
    if (!result.getRemainingScaleNotes().isEmpty()) {
      return fallback(scale, notationKey, accidental);
    }
    return toKeySignature(result, notationKey);
  }

  private static Analyzer.Result chooseBetterOne(Analyzer.Result preferred, Analyzer.Result alternate) {
    if (strict) {
      return preferred;
    }
//    if (!preferred.getRemainingScaleNotes().isEmpty() && alternate.getRemainingScaleNotes().isEmpty()) {
//      return alternate;
//    }
//    // TODO: this check for enharmonic does not change anything (its either both or none of them)
//    // NONONO: its because analyzer is only called for the parent, not the modes!!
//    if (preferred.isEnharmonicRoot() != alternate.isEnharmonicRoot()) {
//      throw new IllegalStateException("xxx");
//    }
//    if (preferred.isEnharmonicRoot() && !alternate.isEnharmonicRoot()) {
//      return alternate;
//    }
    if (!preferred.getMajorNotesWithDoubleAccidental().isEmpty() && alternate.getMajorNotesWithDoubleAccidental().isEmpty()) {
      return alternate;
    }
    return preferred;
  }

  public static KeySignature fromMajorScale(Scale majorScale) {
    return fromScale(majorScale, majorScale.getRoot(), Accidental.fromMajorKey(majorScale.getRoot()));
  }
  
  public static KeySignature fallback(Scale scale, Accidental accidental) {
    return fallback(scale, Note.C, accidental);
  }
  
  public static KeySignature fallback(Scale scale, Note notationKey, Accidental accidental) {
    Map<Note, String> notationMap = Stream.of(Note.values()).collect(Collectors.toMap(Function.identity(), n -> n.getName(accidental)));
    return new KeySignature(notationKey, accidental, notationMap);
  }

  private static KeySignature toKeySignature(Result result) {
    return toKeySignature(result, result.getNotationKey());
  }
  
  private static KeySignature toKeySignature(Analyzer.Result result, Note notationKey) {
    return new KeySignature(notationKey, result.getAccidental(), result.getNotationMap());
  }

  public String notateKey() {
    if (suppressStaffSignature) {
      return "C";
    }
    return notate(notationKey);
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
    return accidental.numberOfAccidentals(notationKey);
  }

  public KeySignature forMode(Scale mode) {
    if (strict) {
      return this;
    }
    if (isEnharmonicRoot(mode.getRoot())) {
      return fallback(mode, notationKey, accidental);
    }
    return this;
  }
  
  private boolean isEnharmonicRoot(Note root) {
    // TODO should not rely on strings
    String rootString = notationMap.get(root);
    return rootString.contains("E#")
        || rootString.contains("B#")
        || rootString.contains("Cb")
        || rootString.contains("Fb");
  }


}
