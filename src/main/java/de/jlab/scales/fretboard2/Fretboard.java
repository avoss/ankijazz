package de.jlab.scales.fretboard2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Fretboard {
  
  @lombok.Data
  public static class Box {
    private final int minFret;
    private final int maxFret;
  }
  
  private List<GuitarString> strings = new ArrayList<>();
  private final Tuning tuning;
  private Optional<Box> box = Optional.empty();

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
    OptionalInt min = getMinMaxFret(string -> string.getMinFret()).min();
    if (min.isPresent() && box.isPresent()) {
      return Math.min(min.getAsInt(), box.get().getMinFret());
    }
    if (min.isPresent()) {
      return min.getAsInt();
    }
    return box.get().getMinFret();
  }

  public int getMaxFret() {
    OptionalInt max = getMinMaxFret(string -> string.getMaxFret()).max();
    if (max.isPresent() && box.isPresent()) {
      return Math.max(max.getAsInt(), box.get().getMaxFret());
    }
    if (max.isPresent()) {
      return max.getAsInt();
    }
    return box.get().getMaxFret();
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
  
  public void setBox(Box box) {
    this.box = Optional.of(box);
  }

  public void mark(Position position, Marker marker) {
    mark(position, n -> marker);
  }

  public void markVisible(Note note, Marker marker) {
    int minFret = getMinFret();
    int maxFret = getMaxFret();
    for (GuitarString string : strings) {
      for (int fret = minFret; fret <= maxFret; fret++) {
        if (string.noteOf(fret) == note) {
          string.mark(fret, marker);
        }
      }
    }
  }

  public void markVisible(Scale scale, Marker marker) {
    for (Note note : scale) {
      markVisible(note, marker);
    }
  }

}
