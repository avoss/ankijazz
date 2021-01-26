package de.jlab.scales.fretboard2;

import java.util.ArrayList;
import java.util.List;

import de.jlab.scales.theory.Note;

public class Fretboard {
  private List<GuitarString> strings = new ArrayList<>();
  private Tuning tuning;

  public Fretboard() {
    this(Tuning.STANDARD_TUNING);
  }

  public Fretboard(Tuning tuning) {
    this.tuning = tuning;
    int index = 0;
    for (Note note : tuning.getStrings()) {
      strings.add(new GuitarString(index++, note));
    }
  }

  public List<GuitarString> getStrings() {
    return strings;
  }

  public Tuning getTuning() {
    return tuning;
  }

}
