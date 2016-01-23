package de.jlab.scales.theory;


public enum Note {
  C("I", "C", "C"), 
  Db("bII", "Db", "C#"), 
  D("II", "D", "D"), 
  Eb("bIII", "Eb", "D#"), 
  E("III", "E", "E"), 
  F("IV", "F", "F"), 
  Gb("bV", "Gb", "F#"), 
  G("V", "G", "G"), 
  Ab("bVI", "Ab", "G#"), 
  A("VI", "A", "A"), 
  Bb("bVII", "Bb", "A#"),
  B("VII", "B", "B");
  
  private final String flatName;
  private final String sharpName;
  private final String romanName;
  
  private Note(String romanName, String flatName, String sharpName) {
    this.romanName = romanName;
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
  
  public Note root() { return this; }
  public Note flat9() { return transpose(1); }
  public Note nine() { return transpose(2); }
  public Note minor3() { return transpose(3); }
  public Note sharp9() { return transpose(3); }
  public Note major3() { return transpose(4); }
  public Note four() { return transpose(5); }
  public Note flat5() { return transpose(6); }
  public Note tritone() { return transpose(6); }
  public Note five() { return transpose(7); }
  public Note sharp5() { return transpose(8); }
  public Note minor6() { return transpose(8); }
  public Note major6() { return transpose(9); }
  public Note flat7() { return transpose(10); }
  public Note sharp7() { return transpose(11); }

  public final String romanName(Note upper) {
    return interval(upper).romanName;
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

  public int semitones(Note upper) {
    int semitones = upper.ordinal() - ordinal();
    while (semitones < 0)
      semitones += 12;
    return semitones;
  }

}
