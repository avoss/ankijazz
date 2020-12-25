package de.jlab.scales.midi.song;

import static de.jlab.scales.TestUtils.majorKeySignature;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.stream.IntStream;

import org.junit.Test;

import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;

public class ChordFactoryTest {

  @Test
  public void testChord() {
    ChordFactory factory = new ChordFactory();
    Scale Bb7 = C7.transpose(Bb);
    factory.add(Bb7, 10);
    assertChord("Bb7", Bb7, factory.next(0, majorKeySignature(Eb)));
    assertChord("A#7", Bb7, factory.next(0, majorKeySignature(E)));
    assertChord("B7", Bb7.transpose(1), factory.next(1, majorKeySignature(E)));
  }
  
  @Test
  public void testProbability() {
    ChordFactory factory = new ChordFactory();
    factory.add(C7, 10);
    factory.add(Cmaj7, 1);
    KeySignature keySignature = majorKeySignature(C);
    long count = IntStream.range(0, 100)
      .mapToObj(i -> factory.next(0, keySignature).getScale())
      .filter(scale -> scale == Cmaj7)
      .count();
    assertThat(count).isBetween(2L, 25L);
  }

  private void assertChord(String symbol, Scale scale, Chord chord) {
    assertEquals(symbol, chord.getSymbol());
    assertEquals(scale, chord.getScale());
  }

}
