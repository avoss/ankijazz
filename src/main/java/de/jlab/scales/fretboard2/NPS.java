package de.jlab.scales.fretboard2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class NPS implements Fingering {
  private static final List<Integer> CAGED = List.of(3, 3, 3, 3, 2);
  private static final List<Integer> TWO_NPS = List.of(2, 2, 2, 2, 2);
  
  public static final Fingering C_MAJOR_CAGED = new NPS(Scales.CMajor.superimpose(Note.B), Tuning.STANDARD_TUNING, CAGED);
  public static final Fingering C_MELODIC_MINOR_CAGED = new NPS(Scales.CMelodicMinor.superimpose(Note.A), Tuning.STANDARD_TUNING, CAGED);
  public static final Fingering C_HARMONIC_MINOR_CAGED = new NPS(Scales.CHarmonicMinor.superimpose(Note.D), Tuning.STANDARD_TUNING, CAGED);
  public static final Fingering C_MINOR7_PENTATONIC = new NPS(Scales.CMinor7Pentatonic, Tuning.STANDARD_TUNING, TWO_NPS);
  public static final Fingering C_MINOR6_PENTATONIC = new NPS(Scales.CMinor6Pentatonic, Tuning.STANDARD_TUNING, TWO_NPS);
  
  private List<Position> positions = new ArrayList<>();
  private Tuning tuning;
  private Scale scale;
  private List<Integer> notesPerString;

  static class NpsPosition implements Position {
    private Map<Integer, List<Integer>> map = new HashMap<>();
    private Tuning tuning;

    public NpsPosition(Tuning tuning) {
      this.tuning = tuning;
    }

    @Override
    public Collection<Integer> getFrets(int stringIndex) {
      return map.get(stringIndex);
    }

    public void put(int stringIndex, List<Integer> frets) {
      map.put(stringIndex, frets);
    }

    @Override
    public Tuning getTuning() {
      return tuning;
    }

    @Override
    public int getMinFret() {
      return frets().min().getAsInt();
    }

    @Override
    public int getMaxFret() {
      return frets().max().getAsInt();
    }

    private IntStream frets() {
      return map.values().stream().flatMap(v -> v.stream()).mapToInt(i -> i);
    }

  }

  interface Handler {
    void startPosition();

    void addString(int stringIndex, List<Integer> frets);

    void endPosition();
  }

  class PositionHandler implements Handler {

    private NpsPosition position;
    private int minFret;

    public PositionHandler(int minFret) {
      this.minFret = minFret;
    }

    @Override
    public void startPosition() {
      position = new NpsPosition(tuning);
    }

    @Override
    public void endPosition() {
      positions.add(position);
    }

    @Override
    public void addString(int stringIndex, List<Integer> frets) {
      position.put(stringIndex, adjust(frets));
    }

    private List<Integer> adjust(List<Integer> frets) {
      return frets.stream().map(fret -> (fret < minFret ? fret + 12 : fret)).collect(Collectors.toList());
    }

  }

  class RangeHandler implements Handler {
    int minFret = Integer.MAX_VALUE;
    int maxFret = Integer.MIN_VALUE;

    @Override
    public void startPosition() {
    }

    @Override
    public void endPosition() {
    }

    @Override
    public void addString(int stringIndex, List<Integer> frets) {
      ToIntFunction<Integer> avoidOpenStrings = fret -> (fret == 0 ? 12 : fret);
      minFret = Math.min(minFret, frets.stream().mapToInt(avoidOpenStrings).min().getAsInt());
      maxFret = Math.max(maxFret, frets.stream().mapToInt(avoidOpenStrings).max().getAsInt());
    }

    public int getMaxFret() {
      return maxFret;
    }

    public int getMinFret() {
      return minFret;
    }

  }

  class PositionComparator implements Comparator<Position> {
    private int minFretOfPositionOne;

    PositionComparator(int minFretOfPositionOne) {
      this.minFretOfPositionOne = minFretOfPositionOne;
    }

    @Override
    public int compare(Position p1, Position p2) {
      int fret1 = fret(p1);
      int fret2 = fret(p2);
      return Integer.compare(fret1, fret2);
    }

    private int fret(Position p) {
      int fret = p.getMinFret();
      return fret < minFretOfPositionOne ? fret + 12 : fret;
    }

  }

  private NPS(Scale scale, Tuning tuning, List<Integer> notesPerString) {
    this.scale = scale;
    this.tuning = tuning;
    this.notesPerString = notesPerString;
    for (int stringsToSkip = 0; stringsToSkip < notesPerString.size(); stringsToSkip++) {
      RangeHandler rangeHandler = new RangeHandler();
      process(stringsToSkip, rangeHandler);
      process(stringsToSkip, new PositionHandler(rangeHandler.getMaxFret() - 6));
    }
    sortPositions();
  }

  private void sortPositions() {
    positions.sort(new PositionComparator(positions.get(0).getMinFret()));
  }

  private void process(int stringsToSkip, Handler handler) {
    Iterator<Note> scaleNotesIterator = Utils.loopIterator(scale.asList());
    Iterator<Integer> notesPerStringIterator = Utils.loopIterator(notesPerString);
    for (int i = 0; i < stringsToSkip; i++) {
      Utils.take(scaleNotesIterator, notesPerStringIterator.next());
    }
    processPosition(scaleNotesIterator, notesPerStringIterator, handler);
  }

  private void processPosition(Iterator<Note> scaleNotesIterator, Iterator<Integer> notesPerStringIterator, Handler handler) {
    handler.startPosition();
    int stringIndex = 0;
    for (Note string : tuning.getStrings()) {
      List<Note> notes = Utils.take(scaleNotesIterator, notesPerStringIterator.next());
      handler.addString(stringIndex, toFrets(string, notes));
      stringIndex++;
    }
    handler.endPosition();
  }

  private List<Integer> toFrets(Note string, List<Note> notes) {
    return notes.stream().map(note -> string.semitones(note)).collect(Collectors.toList());
  }

  @Override
  public List<Position> getPositions() {
    return positions;
  }

}
