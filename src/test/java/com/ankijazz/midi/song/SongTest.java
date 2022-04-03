package com.ankijazz.midi.song;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ankijazz.theory.Scales;

public class SongTest {

  @Test
  public void testGetChordForBeat() {
    Bar b1 = Bar.of(chord("1"));
    Bar b2 = Bar.of(chord("2a"), chord("2b"));
    Song.of(b1, b2);
    assertSymbol(b1, "1", "1", "1", "1", "2a", "2a", "2b", "2b", "1");
    assertSymbol(b2, "2a", "2a", "2b", "2b", "1");
  }

  private void assertSymbol(Bar bar, String ...symbols) {
    for (int beat = 0; beat < symbols.length; beat++) {
      assertEquals(symbols[beat], bar.getChordForBeat(beat).getSymbol());
    }
  }

  private Chord chord(String symbol) {
    return new Chord(Scales.Cmaj7, symbol);
  }
  
  @Test
  public void testToString() {
    Bar b1 = Bar.of(chord("Bb7"));
    Bar b2 = Bar.of(chord("CMaj7"), chord("Dm7"));
    Song song = Song.of(b1, b2);
    assertEquals("| Bb7 | CMaj7 Dm7 |", song.toString());
  }

}
