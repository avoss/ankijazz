package de.jlab.scales.fretboard2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import de.jlab.scales.theory.Note;

@lombok.ToString
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
    return marked.getOrDefault(fret, Marker.EMPTY);
  }
  
  public Note noteOf(int fret) {
    return tuning.transpose(fret);
  }

  public int fretOf(Note note) {
    return tuning.semitones(note);
  }
  
  public int getStringIndex() {
    return stringIndex;
  }

  public OptionalInt getMinFret() {
    return nonEmptyMarkerFrets().min();
  }

  public OptionalInt getMaxFret() {
    return nonEmptyMarkerFrets().max();
  }
  
  IntStream nonEmptyMarkerFrets() {
    return marked.keySet().stream().filter(i -> marked.get(i) != Marker.EMPTY).mapToInt(i -> i);
  }

  public void apply(MarkerRenderer renderer) {
    if (getMinFret().isEmpty() || getMaxFret().isEmpty()) {
      return;
    }
    for (int fret = getMinFret().getAsInt(); fret <= getMaxFret().getAsInt(); fret++) {
      Marker marker = markerOf(fret);
      marker.render(renderer, this, fret);
    }
  }

}
