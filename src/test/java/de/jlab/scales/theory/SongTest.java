package de.jlab.scales.theory;

import org.junit.Before;
import org.junit.Test;

public class SongTest {
  
  private Song song;

  @Before
  public void setUp() {
    song = new Song(new ScaleUniverse(Accidental.FLAT, ScaleType.Major, ScaleType.MelodicMinor, ScaleType.HarmonicMinor));
  }

  @Test(expected = ParseChordException.class)
  public void testInvalidChord() {
    song.parseChords("xy");
  }
  @Test
  public void test() {
    song.parseChords("Em7 B7");
    System.out.println(song);
  }

}
