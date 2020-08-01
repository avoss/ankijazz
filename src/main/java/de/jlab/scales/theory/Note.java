package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;

public enum Note {
  C("1", "C", "C", SHARP), 
  Db("b2", "Db", "C#", FLAT), 
  D("2", "D", "D", SHARP), 
  Eb("b3", "Eb", "D#", FLAT),
  E("3", "E", "E", SHARP), 
  F("4", "F", "F", FLAT), 
  Gb("b5", "Gb", "F#", SHARP), 
  G("5", "G", "G", SHARP),
  Ab("b6", "Ab", "G#", FLAT), 
  A("6", "A", "A", SHARP), 
  Bb("b7", "Bb", "A#", FLAT), 
  B("7", "B", "B", SHARP);

  private final String flatName;
  private final String sharpName;
  private final String intervalName;
  private final Accidental accidental;

  Note(String intervalName, String flatName, String sharpName, Accidental accidental) {
    this.intervalName = intervalName;
    this.flatName = flatName;
    this.sharpName = sharpName;
    this.accidental = accidental;
  }

  public Note transpose(int semitones) {
    int index = ordinal() + semitones;
    while (index < 0)
      index += 1200;
    index = index % 12;
    return values()[index];
  }

  public Note root() {
    return this;
  }

  public Note flat9() {
    return transpose(1);
  }

  public Note nine() {
    return transpose(2);
  }

  public Note minor3() {
    return transpose(3);
  }

  public Note sharp9() {
    return transpose(3);
  }

  public Note major3() {
    return transpose(4);
  }

  public Note four() {
    return transpose(5);
  }

  public Note flat5() {
    return transpose(6);
  }

  public Note tritone() {
    return transpose(6);
  }

  public Note five() {
    return transpose(7);
  }

  public Note sharp5() {
    return transpose(8);
  }

  public Note minor6() {
    return transpose(8);
  }

  public Note major6() {
    return transpose(9);
  }

  public Note flat7() {
    return transpose(10);
  }

  public Note sharp7() {
    return transpose(11);
  }

  // TODO should return Interval instance
  public final String intervalName(Note upper) {
    return interval(upper).intervalName;
  }

  private Note interval(Note upper) {
    return upper.transpose(-ordinal());
  }

  public Note transpose(Note root) {
    return transpose(root.ordinal() - C.ordinal());
  }

  public String getName(Accidental accidental) {
    return accidental == Accidental.FLAT ? flatName : sharpName;
  }

  public String getBothNames() {
    if (flatName.equals(sharpName)) {
      return flatName;
    }
    return sharpName + "/" + flatName;
  }
  
  public Accidental getAccidental() {
    return accidental;
  }

  public int semitones(Note upper) {
    int semitones = upper.ordinal() - ordinal();
    while (semitones < 0)
      semitones += 12;
    return semitones;
  }

  public boolean isSemitone(Note other) {
    return this.semitones(other) == 1 || other.semitones(this) == 1;
  }
  
  public static final Note[] NATURALS = {C, D, E, F, G, A, B};


}
