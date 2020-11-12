package de.jlab.scales.lily;

public enum Clef {
  TREBLE("treble", "e''", "e'", "Treble"),
  BASS("bass", "g", "g,", "Bass");
  
  private String clef;
  private String descendingRelativeTo;
  private String ascendingRelativeTo;
  private String label;

  Clef(String clef, String descendingRelativeTo, String ascendingRelativeTo, String label) {
    this.clef = clef;
    this.descendingRelativeTo = descendingRelativeTo;
    this.ascendingRelativeTo = ascendingRelativeTo;
    this.label = label;
  }
  
  public String getClef() {
    return clef;
  }
  
  public String getRelativeTo(Direction direction) {
    return direction == Direction.ASCENDING ? ascendingRelativeTo : descendingRelativeTo;
  }

  public String getLabel() {
    return label;
  }
}
