package com.ankijazz.theory;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ankijazz.theory.Analyzer.Result;

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
    return new KeySignature(notationKey, result);
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
  
  public KeySignature(Note notationKey, Result result) {
    this(notationKey, result.getAccidental(), result.getAccidentalMap(), result.getNumberOfAccidentals());
  }
  
  public KeySignature(Note notationKey, Accidental accidental, Map<Note, Accidental> accidentalMap, int numberOfAccidentals) {
    this.notationKey = notationKey;
    this.accidental = accidental;
    this.accidentalMap = accidentalMap;
    this.numberOfAccidentals = numberOfAccidentals;
    this.notationMap = accidentalMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().inverse().apply(e.getKey()).name() + e.getValue().symbol()));
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
  
  public String notate(List<Note> notes) {
    return notes.stream().map(this::notate).collect(joining(" "));
  }
  
  // does not preserve order, use for debug/test only
  public String toString(Scale scale) {
    return notate(scale.asList());
  }

  public int getNumberOfAccidentals() {
    return numberOfAccidentals;
  }

  public boolean hasAccidental(Note note) {
    return !accidentalMap.get(note).equals(Accidental.NONE);
  }


}
