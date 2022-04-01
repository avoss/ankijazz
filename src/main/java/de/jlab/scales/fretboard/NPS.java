package de.jlab.scales.fretboard2;

import static de.jlab.scales.fretboard2.Tunings.STANDARD_TUNING;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.Scales;

public class NPS {
  private static final List<Integer> CAGED = List.of(3, 3, 3, 3, 2);
  private static final List<Integer> TWO_NPS = List.of(2, 2, 2, 2, 2);
  private static final List<Integer> THREE_NPS = List.of(3, 3, 3, 3, 3, 3, 3);
  
  public static final Fingering C_MAJOR_CAGED = new NPS("MajorCAGED", Scales.CMajor, Note.B, STANDARD_TUNING, CAGED).create();
  public static final Fingering C_MELODIC_MINOR_CAGED = new NPS("MelodicMinorCAGED", Scales.CMelodicMinor, Note.A, STANDARD_TUNING, CAGED).create();
  public static final Fingering C_HARMONIC_MINOR_CAGED = new NPS("HarmonicMinorCAGED", Scales.CHarmonicMinor, Note.D, STANDARD_TUNING, CAGED).create();

  public static final Fingering C_MAJOR_3NPS = new NPS("Major3NPS", Scales.CMajor, Note.B, STANDARD_TUNING, THREE_NPS).create();
  public static final Fingering C_MELODIC_MINOR_3NPS = new NPS("MelodicMinor3NPS", Scales.CMelodicMinor, Note.A, STANDARD_TUNING, THREE_NPS).create();
  public static final Fingering C_HARMONIC_MINOR_3NPS = new NPS("HarmonicMinor3NPS", Scales.CHarmonicMinor, Note.D, STANDARD_TUNING, THREE_NPS).create();
  
  public static final Fingering C_MINOR7_PENTATONIC = new NPS("Minor7Pentatonic2NPS", Scales.CMinor7Pentatonic, Note.C, STANDARD_TUNING, TWO_NPS).create();
  public static final Fingering C_MINOR6_PENTATONIC = new NPS("Minor6Pentatonic2NPS", Scales.CMinor6Pentatonic, Note.C, STANDARD_TUNING, TWO_NPS).create();

  public static List<Fingering> caged() {
    return List.of(C_MAJOR_CAGED, C_MELODIC_MINOR_CAGED, C_HARMONIC_MINOR_CAGED);
  }
  
  public static Fingering caged(ScaleType type) {
    if (type.equals(Major)) {
      return C_MAJOR_CAGED;
    }
    if (type.equals(MelodicMinor)) {
      return C_MELODIC_MINOR_CAGED;
    }
    if (type.equals(HarmonicMinor)) {
      return C_HARMONIC_MINOR_CAGED;
    }
    if (type.equals(Minor7Pentatonic)) {
      return C_MINOR7_PENTATONIC;
    }
    if (type.equals(Minor6Pentatonic)) {
      return C_MINOR6_PENTATONIC;
    }
    throw new IllegalArgumentException("Unsupported scale type: " + type);
  }
  
  public static List<Fingering> threenps() {
    return List.of(C_MAJOR_3NPS, C_MELODIC_MINOR_3NPS, C_HARMONIC_MINOR_3NPS);
  }
  
  public static List<Fingering> pentatonics() {
    return List.of(C_MINOR6_PENTATONIC, C_MINOR7_PENTATONIC);
  }
  
  public static List<Fingering> allFingerings() {
    return Stream.concat(Stream.concat(caged().stream(), threenps().stream()), pentatonics().stream()).collect(Collectors.toList());
  }
  
  private List<Position> positions = new ArrayList<>();
  private Tuning tuning;
  private Scale scale;
  private List<Integer> notesPerString;
  private Note lowestNote;
  private String name;

  @lombok.Getter
  static class NpsFingering implements Fingering {
    private final Scale scale;
    private final List<Position> positions;
    private String name;

    NpsFingering(String name, Scale scale, List<Position> positions) {
      this.name = name;
      this.scale = scale;
      this.positions = positions;
    }

    @Override
    public Fingering transpose(Note newRoot) {
      int semitones = newRoot.ordinal() - scale.getRoot().ordinal();
      List<Position> transposedPositions = positions.stream().map(p -> p.transpose(semitones)).collect(Collectors.toList());
      Scale transposedScale = scale.transpose(newRoot);
      return new NpsFingering(name, transposedScale, transposedPositions);
    }
    
    @Override
    public String toString() {
      return name;
    }
  }
  
  @lombok.EqualsAndHashCode
  static class NpsPosition implements Position {
    private Map<Integer, List<Integer>> stringToFretsMap;
    private final Tuning tuning;
    private final Scale scale;

    private static final Map<Map<Integer, List<Integer>>, Map<Integer, List<Integer>>> EXCEPTIONS = new HashMap<>();
    
    static {
      EXCEPTIONS.put(parse("8 10 11|8 10 11|9 10|7 8 10|8 9 12|8 10 11"), parse("8 10 11|8 10 11|9 10|7 8 10|8 9|7 8 10"));
      EXCEPTIONS.put(parse("8 10 11|8 10|7 9 10|7 8 10|8 10 12|8 10 11"), parse("7 8 10|6 8 10|7 9 10|7 8 10|8 10|7 8 10"));
      EXCEPTIONS.put(parse("13 15|12 14 15|12 13 15|12 14 16|13 15 16|13 15"), parse("11 13 15|12 14 15|12 13 15|12 14|12 13 15|11 13 15"));
    }
    
    // see NpsPosition.toString()
    static Map<Integer, List<Integer>> parse(String board) {
      String[] strings = board.split("\\|");
      Map<Integer, List<Integer>> stringToFretsMap = new HashMap<>();
      for (int i = 0; i < strings.length; i++) {
        List<Integer> frets = Arrays.stream(strings[i].split(" ")).map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        stringToFretsMap.put(i, frets);
      }
      return stringToFretsMap;
    }
    
    public NpsPosition(Tuning tuning, Scale scale) {
      this(tuning, scale, new HashMap<>());
    }

    public NpsPosition(Tuning tuning, Scale scale, Map<Integer, List<Integer>> stringToFretsMap) {
      this.tuning = tuning;
      this.scale = scale;
      this.stringToFretsMap = stringToFretsMap;
    }

    @Override
    public Collection<Integer> getFrets(int stringIndex) {
      return stringToFretsMap.get(stringIndex);
    }

    public void put(int stringIndex, List<Integer> frets) {
      stringToFretsMap.put(stringIndex, frets);
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
      return stringToFretsMap.values().stream().flatMap(v -> v.stream()).mapToInt(i -> i);
    }
    
    @Override
    public Scale getScale() {
      return scale;
    }

    @Override
    public Position transpose(int semitones) {
      Map<Integer, List<Integer>> transposed = new HashMap<>();
      stringToFretsMap.forEach((string, frets) -> {
        List<Integer> newFrets = frets.stream().map(i -> i + semitones).collect(toList());
        transposed.put(string, newFrets);
      });
      Position transposedPosition = new NpsPosition(tuning, scale, transposed);
      if (transposedPosition.getMinFret() > 12) {
        transposedPosition = transposedPosition.transpose(-12);
      }
      else if (transposedPosition.getMinFret() < 1) {
        transposedPosition = transposedPosition.transpose(12);
      }
      return transposedPosition;
    }
    
    @Override
    public String toString() {
      return IntStream.range(0, getTuning().getStrings().size()).mapToObj(stringIndex -> {
        return getFrets(stringIndex).stream().map(fret -> Integer.toString(fret)).collect(Collectors.joining(" "));
      }).collect(Collectors.joining("|"));
    }

    public void applyExceptions() {
      this.stringToFretsMap = EXCEPTIONS.getOrDefault(stringToFretsMap, stringToFretsMap);
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
      position = new NpsPosition(tuning, scale);
    }

    @Override
    public void endPosition() {
      position.applyExceptions();
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

  private NPS(String name, Scale scale, Note lowestNote, Tuning tuning, List<Integer> notesPerString) {
    this.name = name;
    this.scale = scale;
    this.lowestNote = lowestNote;
    this.tuning = tuning;
    this.notesPerString = notesPerString;
  }

  private Fingering create() {
    positions.clear();
    for (int stringsToSkip = 0; stringsToSkip < notesPerString.size(); stringsToSkip++) {
      RangeHandler rangeHandler = new RangeHandler();
      process(stringsToSkip, rangeHandler);
      process(stringsToSkip, new PositionHandler(rangeHandler.getMaxFret() - 6));
    }
    sortPositions();
    return new NpsFingering(name, scale, positions);
  }

  private void sortPositions() {
    positions.sort(new PositionComparator(positions.get(0).getMinFret()));
  }

  private void process(int stringsToSkip, Handler handler) {
    Iterator<Note> scaleNotesIterator = Utils.loopIterator(scale.superimpose(lowestNote).asList());
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

}
