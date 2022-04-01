package de.jlab.scales.sheet;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.midi.song.MidiTestUtils;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.sheet.PngImageRenderer;
import de.jlab.scales.sheet.RenderContext;


public class PngImageRendererTest {
  RenderContext ctx = RenderContext.ANKI;

  @Test
  public void testTwoChordsInOneBar() {
    Song song = MidiTestUtils.songWith2ChordPerBar();
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    TestUtils.assertImageMatches(renderer.render(), getClass(), "TwoChordsPerBar.png");
  }

}
