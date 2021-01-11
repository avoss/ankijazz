package de.jlab.scales.midi.song;

import static de.jlab.scales.theory.BuiltinChordType.AugmentedTriad;
import static de.jlab.scales.theory.BuiltinChordType.Diminished7;
import static de.jlab.scales.theory.BuiltinChordType.DiminishedTriad;
import static de.jlab.scales.theory.BuiltinChordType.Dominant13;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat5;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat5flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat5sharp9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5sharp9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sus4;
import static de.jlab.scales.theory.BuiltinChordType.Dominant9;
import static de.jlab.scales.theory.BuiltinChordType.Major6;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinChordType.Major7Sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Major7Sharp5;
import static de.jlab.scales.theory.BuiltinChordType.Major9;
import static de.jlab.scales.theory.BuiltinChordType.MajorTriad;
import static de.jlab.scales.theory.BuiltinChordType.Minor11;
import static de.jlab.scales.theory.BuiltinChordType.Minor6;
import static de.jlab.scales.theory.BuiltinChordType.Minor7;
import static de.jlab.scales.theory.BuiltinChordType.Minor7b5;
import static de.jlab.scales.theory.BuiltinChordType.Minor9;
import static de.jlab.scales.theory.BuiltinChordType.MinorMajor7;
import static de.jlab.scales.theory.BuiltinChordType.MinorTriad;
import static de.jlab.scales.theory.BuiltinChordType.Sus4Triad;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.Scales;

public class SongDifficultyModel {

  static final ScaleType[] typesOrderedByDifficulty = {
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
      Dominant13,
      AugmentedTriad,
      Minor11,
      Dominant7sharp5,
      Dominant7sharp9,
      Dominant7flat5,
      Major7Sharp11,

      MinorMajor7,
      Dominant7flat5flat9,
      Dominant7sharp5sharp9,
      Dominant7sharp5flat9,
      Dominant7flat5sharp9,
      
      Major7Sharp5,
      
  };
  
  private static final List<Scale> scalesOrderedByDifficulty = 
      Arrays.stream(typesOrderedByDifficulty)
      .map(ScaleType::getPrototype)
      .collect(Collectors.toList()); 
  

  public double getDifficulty(Song song) {
    List<Scale> chords = song.getBars().stream()
    .flatMap(bar -> bar.getChords().stream())
    .map(chord -> chord.getScale())
    .collect(toList());
    
    IntSummaryStatistics statistics = chords.stream()
      .mapToInt(this::getChordDifficulty)
      .summaryStatistics();
    double chordDifficulty = statistics.getAverage() / scalesOrderedByDifficulty.size();
    double numberOfDifferentChordsDifficulty = (double)chords.stream().collect(toSet()).size() / chords.size();
    
    double numberOfAccidentals = (double)chords.stream()
      .flatMap(chord -> chord.asList().stream())
      .filter(note -> !Scales.CMajor.contains(note))
      .collect(toSet()).size() / Note.values().length;

    // System.out.println(String.format("%5f %5f %5f %s", chordDifficulty, numberOfDifferentChordsDifficulty, numberOfAccidentals, song));

    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(120).update(chordDifficulty);
    model.doubleTerm(50).update(numberOfDifferentChordsDifficulty);
    model.doubleTerm(30).update(numberOfAccidentals);
    return model.getDifficulty();
  }

  int getChordDifficulty(Scale chord) {
    Scale transposed = chord.transpose(Note.C);
    int index = scalesOrderedByDifficulty.indexOf(transposed);
    if (index < 0) {
      throw new IllegalArgumentException("Chord not found in difficulty list: " + chord.asChord());
    }
    return index;
  }

}
