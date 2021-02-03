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
    return marked.getOrDefault(fret, Markers.empty());
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
    return nonEmptyMarkers().min();
  }

  public OptionalInt getMaxFret() {
    return nonEmptyMarkers().max();
  }
  
  private IntStream nonEmptyMarkers() {
    return marked.keySet().stream().filter(i -> !marked.get(i).isEmpty()).mapToInt(i -> i);
  }


}
