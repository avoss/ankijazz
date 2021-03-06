package com.ankijazz.theory;

public enum Note {
  C("1", "C", "C"), 
  Db("b2", "Db", "C#"), 
  D("2", "D", "D"), 
  Eb("b3", "Eb", "D#"),
  E("3", "E", "E"), 
  F("4", "F", "F"), 
  Gb("b5", "Gb", "F#"), 
  G("5", "G", "G"),
  Ab("b6", "Ab", "G#"), 
  A("6", "A", "A"), 
  Bb("b7", "Bb", "A#"), 
  B("7", "B", "B");

  private final String flatName;
  private final String sharpName;
  private final String intervalName;

  Note(String intervalName, String flatName, String sharpName) {
    this.intervalName = intervalName;
    this.flatName = flatName;
    this.sharpName = sharpName;
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

  public Note sharp4() {
    return transpose(6);
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

  public Note minor7() {
    return transpose(10);
  }

  public Note major7() {
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
    return flatName + "/" + sharpName;
  }
  
  public int semitones(Note upper) {
    int semitones = upper.ordinal() - ordinal();
    while (semitones < 0)
      semitones += 12;
    return semitones;
  }

  public int distance(Note that) {
    return Math.min(this.semitones(that), that.semitones(this));
  }
  
  public boolean isSemitone(Note other) {
    return this.semitones(other) == 1 || other.semitones(this) == 1;
  }

  
}
