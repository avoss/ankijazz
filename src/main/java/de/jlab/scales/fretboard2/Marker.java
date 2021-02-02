package de.jlab.scales.fretboard2;

public interface Marker {
  void render(MarkerRenderer renderer, GuitarString string, int fret);
  default boolean isEmpty() {
    return false;
  }
}
