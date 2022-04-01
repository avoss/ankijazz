package de.jlab.scales.fretboard;

public interface MarkerRenderer {

  void renderEmpty(GuitarString string, int fret);

  void renderForeground(GuitarString string, int fret);

  void renderBackground(GuitarString string, int fret);

  void renderRoot(GuitarString string, int fret);

}
