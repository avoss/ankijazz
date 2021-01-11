package de.jlab.scales.jtg;

import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.midi.song.MidiTestUtils;
import de.jlab.scales.midi.song.Song;


public class PngImageRendererTest {
  RenderContext ctx = RenderContext.ANKI;
  Path dir = Paths.get("build/PngImageRendererTest");

  @Test
  public void testAnkiImage() {
    Song song = MidiTestUtils.randomSong(ctx.getNumberOfBars());
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    Path path = dir.resolve("OneChordPerBar.png");
    renderer.renderTo(path);
    assertTrue(path.toFile().exists());
  }
  
  @Test
  public void testTwoChordsInOneBar() {
    Song song = MidiTestUtils.songWith2ChordPerBar();
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    Path path = dir.resolve("TwoChordsPerBar.png");
    renderer.renderTo(path);
    assertTrue(path.toFile().exists());
  }


}
