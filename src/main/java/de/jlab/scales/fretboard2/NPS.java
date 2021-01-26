package de.jlab.scales.fretboard2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import de.jlab.scales.Utils;
import de.jlab.scales.fretboard2.NPS.RangeHandler;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class NPS implements Fingering {
  public static Fingering CMajor = new NPS(Scales.CMajor.superimpose(Note.B), Tuning.STANDARD_TUNING, List.of(3, 3, 3, 3, 2));
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

  NPS(Scale scale, Tuning tuning, List<Integer> notesPerString) {
    this.scale = scale;
    this.tuning = tuning;
    this.notesPerString = notesPerString;
    for (int stringSkip = 0; stringSkip < notesPerString.size(); stringSkip++) {
      RangeHandler rangeHandler = new RangeHandler();
      process(stringSkip, rangeHandler);
      process(stringSkip, new PositionHandler(rangeHandler.getMaxFret() - 6));
    }
  }

  private void process(int stringSkip, Handler handler) {
    Iterator<Note> scaleNotesIterator = Utils.loopIterator(scale.asList());
    Iterator<Integer> notesPerStringIterator = Utils.loopIterator(notesPerString);
    for (int i = 0; i < stringSkip; i++) {
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
