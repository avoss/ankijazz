package de.jlab.scales.jtg;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.midi.song.MidiTestUtils;
import de.jlab.scales.midi.song.Song;


public class PngImageRendererTest {
  RenderContext ctx = RenderContext.ANKI;

  @Test
  public void testTwoChordsInOneBar() {
    Song song = MidiTestUtils.songWith2ChordPerBar();
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    TestUtils.assertImageMatches(renderer.render(), getClass(), "TwoChordsPerBar.png");
  }

}
