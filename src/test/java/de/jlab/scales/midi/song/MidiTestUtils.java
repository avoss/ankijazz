package de.jlab.scales.midi.song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class MidiTestUtils {

  
  private static ThreadLocalRandom random = ThreadLocalRandom.current();

  public static Event event(int patternLength, int patternIndex, int velocity, int length) {
    return Event.builder()
        .patternLength(patternLength)
        .patternIndex(patternIndex)
        .eventId("0:".concat(Integer.toString(patternIndex)))
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

  public static Song songWith2ChordPerBar() {
    Bar b1 = Bar.of(Chord.of(Scales.Cm7.transpose(Note.D), "Dm7"), Chord.of(Scales.C7.transpose(Note.G), "G7"));
    Bar b2 = Bar.of(Chord.of(Scales.Cmaj7, "CMA7"));
    List<Bar> bars = new ArrayList<>();
    IntStream.range(0, 4).forEach(i -> {
      bars.add(b1);
      bars.add(b2);
      bars.add(b2);
      bars.add(b1);
    });
    return new Song(bars);
  }

}
