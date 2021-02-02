package de.jlab.scales.fretboard2;

import java.util.HashMap;
import java.util.Map;

import de.jlab.scales.theory.Note;

public class GuitarString {

  private Note tuning;
  private Map<Integer, Marker> marked = new HashMap<>();
  private int stringIndex;

  public GuitarString(int stringIndex, Note tuning) {
    this.stringIndex = stringIndex;
    this.tuning = tuning;
  }

  public void mark(int fret, Marker marker) {
    marked.put(fret, marker);
  }
  
  public Marker markerOf(int fret) {
    return marked.getOrDefault(fret, Markers.empty());
  }
  
  public Note noteOf(int fret) {
    return tuning.transpose(fret);
  }
  
  public int getStringIndex() {
    return stringIndex;
  }

}
