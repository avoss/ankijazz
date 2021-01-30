package de.jlab.scales.fretboard2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

  public Fretboard(Position position, Function<Note, Marker> markers) {
    this(position.getTuning());
    for (int i = 0; i < tuning.getStrings().size(); i++) {
      GuitarString string = strings.get(i);
      for (int fret : position.getFrets(i)) {
        Marker marker = markers.apply(string.noteOf(fret));
        string.mark(fret, marker);
      }
    }
  }

  public List<GuitarString> getStrings() {
    return strings;
  }

  public Tuning getTuning() {
    return tuning;
  }

}
