package de.jlab.scales.midi.song;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.ChordParser;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class MidiTestUtils {

  
  private static ThreadLocalRandom random = ThreadLocalRandom.current();

  public static Event event(int patternIndex, int velocity, int length) {
    return event(patternIndex, velocity, length, patternIndex/4);
  }

  public static Event event(int patternIndex, int velocity, int length, int beat) {
    return event(0, patternIndex, velocity, length, beat);
  }
    
  public static Event event(int patternId, int patternIndex, int velocity, int length, int beat) {
    return Event.builder()
        .patternId(patternId)
        .patternIndex(patternIndex)
        .velocity(velocity)
        .noteLength(length)
        .beat(beat)
        .build();
  }
  
  public static Song staticSong() {
    return song("Cm7 F7");
  }
  
  public static Song song(String chords) {
    List<Bar> bars = Arrays.stream(chords.split("\\s+"))
      .map(symbol -> Chord.of(ChordParser.parseChord(symbol), symbol))
      .map(chord -> Bar.of(chord))
      .collect(toList());
    return Song.of(bars);
  }
  
  public static SongWrapper createStaticSongWrapper() {
    return SongWrapper.builder()
      .song(staticSong())
      .key("Key of C")
      .progression("Test Progression")
      .type("Minor7Chords")
      .build();
  }
  
  
  public static Song randomSong(int numberOfBars) {
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
