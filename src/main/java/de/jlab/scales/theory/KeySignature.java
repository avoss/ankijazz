package de.jlab.scales.theory;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import de.jlab.scales.theory.Analyzer.Result;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class KeySignature {
  @EqualsAndHashCode.Include
  private final Note notationKey;
  @EqualsAndHashCode.Include
  private final Accidental accidental;
  @EqualsAndHashCode.Include
  private final Map<Note, String> notationMap;
  @EqualsAndHashCode.Include
  private boolean suppressStaffSignature = false;
  @EqualsAndHashCode.Include
  private final int numberOfAccidentals;
  private final Map<Note, Accidental> accidentalMap;
  
  public KeySignature suppressStaffSignature() {
    KeySignature keySignature = new KeySignature(Note.C, accidental, notationMap, numberOfAccidentals, accidentalMap);
    keySignature.suppressStaffSignature = true;
    return keySignature;
  }

  public static KeySignature fromScale(Scale scale, Note notationKey, Accidental accidental) {
    Analyzer analyzer = new Analyzer();
    Result result = analyzer.analyzeScale(scale, accidental);
    return toKeySignature(scale, result, notationKey);
  }
  
  /**
   * according to Frank Sikora's book, every minor scale is notated using the key signature 
   * of relative major.
   */
  public static KeySignature fromScale(Scale scale) {
    Note notationKey = scale.isMinor() ? scale.getRoot().transpose(3) : scale.getRoot();
    Accidental accidental = Accidental.preferredAccidentalForMajorKey(notationKey);
    return fromScale(scale, notationKey, accidental);
  }
  
  private static KeySignature toKeySignature(Scale scale, Result result, Note notationKey) {
    int numberOfAccidentals = 
        result.getMajorNotesWithAccidental().size() 
        + result.getMajorNotesWithInverseAccidental().size()
        + result.getMajorNotesWithDoubleAccidental().size() * 2;
    return new KeySignature(notationKey, result.getAccidental(), result.getNotationMap(), numberOfAccidentals, result.getAccidentalMap());
  }

  public Note getKeySignature() {
    if (suppressStaffSignature) {
      return Note.C;
    }
    return notationKey;
  }
  
  public String getKeySignatureString() {
    return notate(getKeySignature());
  }
  
  public String notate(Note note) {
    return notationMap.get(note);
  }
  
  public List<String> notate(Scale scale) {
    return scale.asList().stream().map(this::notate).collect(toList());
  }
  
  public String toString(Scale scale) {
    return toString(scale.asList());
  }

  public String toString(List<Note> notes) {
    return notes.stream().map(this::notate).collect(joining(" "));
  }
  
  public int getNumberOfAccidentals() {
    return numberOfAccidentals;
  }

  public boolean hasAccidental(Note note) {
    return !accidentalMap.get(note).equals(Accidental.NONE);
  }


}
