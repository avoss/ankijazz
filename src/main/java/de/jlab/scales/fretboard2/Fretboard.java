package de.jlab.scales.fretboard2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;

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
    mark(position, markers);
  }

  public void mark(Position position, Function<Note, Marker> markers) {
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

  public int getMinFret() {
    return getMinMaxFret(string -> string.getMinFret()).min().getAsInt();
  }

  public int getMaxFret() {
    return getMinMaxFret(string -> string.getMaxFret()).max().getAsInt();
  }

  private IntStream getMinMaxFret(Function<GuitarString, OptionalInt> toMinMax) {
    return strings.stream().map(toMinMax).filter(minMax -> minMax.isPresent()).mapToInt(fret -> fret.getAsInt());
  }
  
  public void mark(int string, int fret, Marker marker) {
    strings.get(string).mark(fret, marker);
  }

  public Marker getMarker(int string, int fret) {
    return strings.get(string).markerOf(fret);
  }

  public GuitarString getString(int string) {
    return strings.get(string);
  }

}
