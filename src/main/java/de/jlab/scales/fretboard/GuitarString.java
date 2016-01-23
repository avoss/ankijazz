package de.jlab.scales.fretboard;

import java.util.HashMap;
import java.util.Map;

import de.jlab.scales.theory.Note;

public class GuitarString {
  
  private final Note root;
  private final int length;
  
  private Map<Note, Character> marks = new HashMap<>();

  public GuitarString(Note root, int length) {
    this.root = root;
    this.length = length;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("|");
     // no open strings
    Note fret = root.transpose(1); 
    for (int i = 0; i < length; i++) {
      Character symbol = marks.get(fret);
      symbol = symbol == null ? '-' : symbol;
      sb.append("-").append(symbol).append("-|");
      fret = fret.transpose(1);
    }
    return sb.toString();
  }
  
  public void mark(Note note, char symbol) {
    marks.put(note, symbol);
  }

  public Note getRoot() {
    return root;
  }

  public int getLength() {
    return length;
  }

  public void clear() {
    marks.clear();
  }
  
}
