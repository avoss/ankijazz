package de.jlab.scales.fretboard;

import java.util.HashMap;
import java.util.Map;

import de.jlab.scales.theory.Note;

public class GuitarString {
  
  private final Note root;
  private final int numberOfFrets;
  
  // marks, fret 0 == open string, fret 1..numberOfFrets = fretted notes
  private Map<Integer, Character> marks = new HashMap<>();

  public GuitarString(Note root, int numberOfFrets) {
    this.root = root;
    this.numberOfFrets = numberOfFrets;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("|");
    for (int fret = 1; fret <= numberOfFrets; fret++) {
      Character symbol = '-';
      if (marks.containsKey(fret))
        symbol = String.valueOf(marks.get(fret)).charAt(0);
      sb.append("-").append(symbol).append("-|");
    }
    return sb.toString();
  }
  
  public void mark(int fretNumber, Character symbol) {
    if (fretNumber < 1)
      throw new IllegalArgumentException("Open String can not be marked");
    marks.put(fretNumber, symbol);
  }

  public void markFirst(Note note, Character symbol) {
    Note fret = root.transpose(1);
    for (int i = 1; i < numberOfFrets; i++) {
      if (fret == note) {
        mark(i, symbol);
        break;
      }
      fret = fret.transpose(1);
    }
  }

  public void markAll(Note note, Character symbol) {
    Note fret = root.transpose(1);
    for (int i = 1; i < numberOfFrets; i++) {
      if (fret == note) {
        mark(i, symbol);
      }
      fret = fret.transpose(1);
    }
  }

  public Note getRoot() {
    return root;
  }

  public void clear() {
    marks.clear();
  }
  
}
