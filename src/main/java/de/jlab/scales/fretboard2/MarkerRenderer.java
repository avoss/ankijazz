package de.jlab.scales.fretboard2;

public interface MarkerRenderer {
  public enum LeftRight { LEFT, RIGHT }
  public enum UpDown { UP, DOWN }

  void renderEmpty(GuitarString string, int fret);

  void renderForeground(GuitarString string, int fret);

  void renderBackground(GuitarString string, int fret);

  void renderRoot(GuitarString string, int fret);

}
