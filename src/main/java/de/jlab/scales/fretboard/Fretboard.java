package de.jlab.scales.fretboard;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.G;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import de.jlab.scales.theory.Note;

public class Fretboard {

  private List<GuitarString> strings;
  public static final Note[] STANDARD_TUNING = new Note[] { E, A, D, G, B, E };

  public Fretboard(int numberOfFrets) {
    this(numberOfFrets, STANDARD_TUNING);
  }

  public Fretboard(int numberOfFrets, Note[] tuning) {
    strings = createStrings(numberOfFrets, STANDARD_TUNING);
  }

  public Fretboard(int numberOfFrets, int numberOfStrings) {
    Note note = E;
    Note[] tuning = new Note[numberOfStrings];
    for (int i = 0; i < numberOfStrings; i++) {
      tuning[i] = note;
      note = note.transpose(5);
    }
    strings = createStrings(numberOfFrets, tuning);
  }
  
  private List<GuitarString> createStrings(int numberOfFrets, Note[] roots) {
    List<GuitarString> strings = new ArrayList<GuitarString>();
    for (Note root : roots)
      strings.add(new GuitarString(root, numberOfFrets));
    return strings;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    ListIterator<GuitarString> iter = strings.listIterator(strings.size());
    while (iter.hasPrevious()) {
      GuitarString string = iter.previous();
      sb.append(string.toString()).append("\n");
    }
    return sb.toString();
  }

  public GuitarString getString(int i) {
    return strings.get(i);
  }

  public List<GuitarString> getStrings() {
    return strings;
  }

  public void clear() {
    for (GuitarString s : strings)
      s.clear();
  }

}
