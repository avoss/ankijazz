package de.jlab.scales.jtg;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.MidiTestUtils;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scales;


public class PngImageRendererTest {
  RenderContext ctx = RenderContext.ANKI;
  Path dir = Paths.get("build/PngImageRendererTest");

  @Test
  public void testAnkiImage() {
    Song song = MidiTestUtils.createRandomSong(ctx.getNumberOfBars());
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
