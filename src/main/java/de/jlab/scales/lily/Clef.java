package de.jlab.scales.lily;

public enum Clef {
  TREBLE("treble", "e''", "e'"),
  BASS("bass", "g", "g,");
  
  private String clef;
  private String descendingRelativeTo;
  private String ascendingRelativeTo;

  Clef(String clef, String descendingRelativeTo, String ascendingRelativeTo) {
    this.clef = clef;
    this.descendingRelativeTo = descendingRelativeTo;
    this.ascendingRelativeTo = ascendingRelativeTo;
  }
  
  public String getClef() {
    return clef;
  }
  
  public String getRelativeTo(Direction direction) {
    return direction == Direction.ASCENDING ? ascendingRelativeTo : descendingRelativeTo;
  }
}
