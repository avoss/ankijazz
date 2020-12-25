package de.jlab.scales.midi.song;

import static de.jlab.scales.TestUtils.majorKeySignature;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.stream.IntStream;

import org.junit.Test;

import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;

public class BarFactoryTest {

  @Test
  public void testChord() {
    BarFactory factory = new BarFactory();
    factory.add(10, C7.transpose(G));
    assertChord("C7", C7, factory.next(majorKeySignature(F)));
    assertChord("D7", C7.transpose(D), factory.next(majorKeySignature(G)));
    assertChord("Bb7", C7.transpose(Bb), factory.next(majorKeySignature(Eb)));
    assertChord("F#7", C7.transpose(Gb), factory.next(majorKeySignature(B)));
  }

  @Test
  public void testProbability() {
    BarFactory factory = new BarFactory();
    factory.add(10, C7);
    factory.add(1, Cmaj7);
    KeySignature keySignature = majorKeySignature(C);
    long count = IntStream.range(0, 100)
      .mapToObj(i -> factory.next(keySignature).getChords().get(0).getScale())
      .filter(scale -> scale == Cmaj7)
      .count();
    assertThat(count).isBetween(2L, 25L);
  }

  private void assertChord(String symbol, Scale scale, Bar bar) {
    assertEquals(1, bar.getChords().size());
    Chord chord = bar.getChords().get(0);
    assertEquals(symbol, chord.getSymbol());
    assertEquals(scale, chord.getScale());
  }

}
