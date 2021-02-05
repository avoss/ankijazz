package de.jlab.scales.difficulty;

import static de.jlab.scales.theory.BuiltinChordType.AugmentedTriad;
import static de.jlab.scales.theory.BuiltinChordType.Diminished7;
import static de.jlab.scales.theory.BuiltinChordType.DiminishedTriad;
import static de.jlab.scales.theory.BuiltinChordType.Dominant11;
import static de.jlab.scales.theory.BuiltinChordType.Dominant13;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat13;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat5;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat5flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat5sharp9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat9flat13;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat9sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5sharp9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp9flat13;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp9sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sus4;
import static de.jlab.scales.theory.BuiltinChordType.Dominant9;
import static de.jlab.scales.theory.BuiltinChordType.Major6;
import static de.jlab.scales.theory.BuiltinChordType.Major69;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinChordType.Major7Sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Major7Sharp5;
import static de.jlab.scales.theory.BuiltinChordType.Major9;
import static de.jlab.scales.theory.BuiltinChordType.MajorTriad;
import static de.jlab.scales.theory.BuiltinChordType.Minor11;
import static de.jlab.scales.theory.BuiltinChordType.Minor6;
import static de.jlab.scales.theory.BuiltinChordType.Minor69;
import static de.jlab.scales.theory.BuiltinChordType.Minor7;
import static de.jlab.scales.theory.BuiltinChordType.Minor7b5;
import static de.jlab.scales.theory.BuiltinChordType.Minor9;
import static de.jlab.scales.theory.BuiltinChordType.MinorMajor7;
import static de.jlab.scales.theory.BuiltinChordType.MinorTriad;
import static de.jlab.scales.theory.BuiltinChordType.PowerChord;
import static de.jlab.scales.theory.BuiltinChordType.Sus4Triad;
import static de.jlab.scales.theory.BuiltinScaleType.DiminishedHalfWhole;
import static de.jlab.scales.theory.BuiltinScaleType.Dominant7Pentatonic;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMajor;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static de.jlab.scales.theory.BuiltinScaleType.WholeTone;
import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleType;

public class Difficulties {
  
  private Difficulties() {}

  static final List<ScaleType> CHORD_TYPES_ORDERED_BY_DIFFICULTY = List.of(
      PowerChord,
      MajorTriad,
      MinorTriad,
      Minor7,
      Dominant7,
      Major7,
      Minor7b5,
      Dominant7flat9,
      Major6,
      Major9,
      Minor6,
      Minor9,
      DiminishedTriad,
      Diminished7,
      Dominant9,
      Sus4Triad,
      Dominant7sus4,
      Major69,
      Dominant13,
      AugmentedTriad,
      Minor69,
      Minor11,
      Dominant7sharp5,
      Dominant7flat13,
      Dominant7sharp9,
      Dominant7flat5,
      Dominant7sharp11,
      Major7Sharp11,

      Dominant11,
      MinorMajor7,
      
      Dominant7flat5flat9,
      Dominant7flat5sharp9,
      Dominant7sharp5flat9,
      Dominant7sharp5sharp9,

      Dominant7flat9sharp11,
      Dominant7flat9flat13,
      Dominant7sharp9sharp11,
      Dominant7sharp9flat13,
      
      Major7Sharp5
  );
  
  static final List<ScaleType> SCALE_TYPES_ORDERED_BY_DIFFICULTY = List.of(
      Minor7Pentatonic,
      Major,
      Minor6Pentatonic,
      MelodicMinor,
      HarmonicMinor,
      WholeTone,
      DiminishedHalfWhole,
      Dominant7Pentatonic,
      HarmonicMajor
  );
  
  private static final List<Scale> CHORDS_ORDERED_BY_DIFFICULTY = 
      CHORD_TYPES_ORDERED_BY_DIFFICULTY.stream()
        .map(ScaleType::getPrototype)
        .collect(Collectors.toList()); 
  
  public static double getSongDifficulty(Song song) {
    List<Scale> chords = song.getBars().stream()
      .flatMap(bar -> bar.getChords().stream())
      .map(chord -> chord.getScale())
      .collect(toList());
    
    DoubleSummaryStatistics statistics = chords.stream()
      .mapToDouble(chord -> getChordDifficulty(chord))
      .summaryStatistics();
    double chordDifficulty = statistics.getAverage();
    double numberOfDifferentChordsDifficulty = (double)chords.stream().collect(toSet()).size() / chords.size();
    
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(120).update(chordDifficulty);
    model.doubleTerm(50).update(numberOfDifferentChordsDifficulty);
    return model.getDifficulty();
  }
  
  // TODO replace with ScaleUniverse lookup and use ChordInfoDifficulty()?
  public static double getChordDifficulty(Scale chord) {
    double chordTypeDifficulty = getChordTypeDifficulty(chord);
    double accidentalDifficulty = getAccidentalDifficulty(chord);
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(120).update(chordTypeDifficulty);
    model.doubleTerm(30).update(accidentalDifficulty);
    return model.getDifficulty();
  }

  private static double getAccidentalDifficulty(Scale chord) {
    return (double)chord.asList().stream().filter(note -> !CMajor.contains(note)).collect(toSet()).size() / 6;
  }

  static double getChordTypeDifficulty(Scale chord) {
    return getTypeDifficulty(chord.transpose(Note.C), CHORDS_ORDERED_BY_DIFFICULTY);
  }
  
  private static <T> double getTypeDifficulty(T element, List<T> elements) {
    int index = elements.indexOf(element);
    if (index < 0) {
      throw new IllegalArgumentException("Not found in difficulty list: " + element);
    }
    return (double)index / elements.size();
  }

  public static double getScaleInfoDifficulty(ScaleInfo modeInfo) {
    return getInfoDifficulty(modeInfo, SCALE_TYPES_ORDERED_BY_DIFFICULTY);
  }

  public static double getChordInfoDifficulty(ScaleInfo chordInfo) {
    return getInfoDifficulty(chordInfo, CHORD_TYPES_ORDERED_BY_DIFFICULTY);
  }

  private static double getInfoDifficulty(ScaleInfo modeInfo, List<ScaleType> types) {
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(0, 6, 100).update(modeInfo.getKeySignature().getNumberOfAccidentals());
    model.doubleTerm(100).update(getTypeDifficulty(modeInfo.getScaleType(), types));
    model.booleanTerm(25).update(modeInfo.isInversion());
    return model.getDifficulty();
  }


  
}
