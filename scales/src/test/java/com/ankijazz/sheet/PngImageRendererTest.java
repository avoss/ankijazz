package com.ankijazz.sheet;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.midi.song.MidiTestUtils;
import com.ankijazz.midi.song.Song;


public class PngImageRendererTest {
  RenderContext ctx = RenderContext.ANKI;

  @Test
  public void testTwoChordsInOneBar() {
    Song song = MidiTestUtils.songWith2ChordPerBar();
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    TestUtils.assertImageMatches(renderer.render(), getClass(), "TwoChordsPerBar.png");
  }

}
