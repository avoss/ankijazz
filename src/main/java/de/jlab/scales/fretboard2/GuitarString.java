package de.jlab.scales.fretboard2;

import java.util.HashSet;
import java.util.Set;

import de.jlab.scales.theory.Note;

public class GuitarString {

  private Note tuning;
  private Set<Integer> marked = new HashSet<>();
  private int stringIndex;

  public GuitarString(int index, Note tuning) {
    this.stringIndex = index;
    this.tuning = tuning;
  }

  public void mark(int fret) {
    marked.add(fret);
  }
  
  public Set<Integer> getMarked() {
    return marked;
  }

  public Note noteOf(int fret) {
    return tuning.transpose(fret);
  }
  
  public int getStringIndex() {
    return stringIndex;
  }

}
