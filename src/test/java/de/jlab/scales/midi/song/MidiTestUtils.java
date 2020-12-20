package de.jlab.scales.midi.song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class MidiTestUtils {

  public static final int PATTERN_ID = 27;
  
  private static ThreadLocalRandom random = ThreadLocalRandom.current();

  public static Event event(int patternLength, int patternIndex, int velocity, int length) {
    return Event.builder()
        .patternLength(patternLength)
        .patternIndex(patternIndex)
        .patternId(PATTERN_ID)
        .velocity(velocity)
        .noteLength(length)
        .build();
  }
  
  public static Song createStaticSong() {
    return new Song(List.of(Bar.of(Chord.of(Scales.Cm7, "Cm7")), Bar.of(Chord.of(Scales.C7.transpose(Note.F), "F7"))));
  }
  
  public static Song createRandomSong(int numberOfBars) {
    List<Bar> bars = new ArrayList<>();
    for (int i = 0; i < numberOfBars; i++) {
      bars.add(Bar.of(randomChord()));
    }
    return new Song(bars);
  }

  private static Chord randomChord() {
    BuiltinChordType[] types = BuiltinChordType.values();
    BuiltinChordType type = types[random.nextInt(types.length)];
    Note[] notes = Note.values();
    Note root = notes[random.nextInt(notes.length)];
    Scale scale = type.getPrototype().transpose(root);
    String symbol = ScaleUniverse.CHORDS.findFirstOrElseThrow(scale).getScaleName();
    return new Chord(scale, symbol);
  }

}
