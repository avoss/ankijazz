package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import de.jlab.scales.theory.Analyzer.Result;
import lombok.*;

/**
 * TODO: 
 * - if (enharmonic root or double accidental or missing scale notes) { return fallback }
 * - fallback should have badness
 * 
 * @author andreas
 *
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class KeySignature {
  private final Scale constructionScale;
  @EqualsAndHashCode.Include
  private final Note notationKey;
  @EqualsAndHashCode.Include
  private final Accidental accidental;
  @EqualsAndHashCode.Include
  private final Map<Note, String> notationMap;
  @EqualsAndHashCode.Include
  private boolean suppressStaffSignature = false;
  
  public KeySignature suppressStaffSignature() {
    KeySignature keySignature = new KeySignature(constructionScale, Note.C, accidental, notationMap);
    keySignature.suppressStaffSignature = true;
    return keySignature;
  }

  public static KeySignature fromScale(Scale scale, Note notationKey, Accidental accidental) {
    // TODO should work for non-major scales like AmMaj7?
    if (scale.getNumberOfNotes() != CMajor.getNumberOfNotes())  {
      return fallback(scale, notationKey, accidental);
    }
    Analyzer analyzer = new Analyzer();
    Result result = analyzer.analyze(scale, accidental);
    if (!result.getRemainingScaleNotes().isEmpty()) {
      return fallback(scale, notationKey, accidental);
    }
    return toKeySignature(scale, result, notationKey);
  }
  
  public KeySignature inverse() {
    return fromScale(constructionScale, notationKey, accidental.inverse());
  }
  
  /**
   * according to Frank Sikora's book, every minor scale is notated using the key signature 
   * of relative major.
   */
  public static KeySignature fromScale(Scale scale) {
    Note notationKey = scale.isMinor() ? scale.getRoot().transpose(3) : scale.getRoot();
    return fromScale(scale, notationKey, Accidental.preferredAccidentalForMajorKey(notationKey));
  }
  
  public static KeySignature fallback(Scale scale) {
    return fallback(scale, Note.C, FLAT);
  }
  
  public static KeySignature fallback(Scale scale, Note notationKey, Accidental accidental) {
    return toKeySignature(scale, new Analyzer().fallback(scale, accidental), notationKey);
  }

  private static KeySignature toKeySignature(Scale scale, Result result, Note notationKey) {
    return new KeySignature(scale, notationKey, result.getAccidental(), result.getNotationMap());
  }

  public String notationKey() {
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


}
