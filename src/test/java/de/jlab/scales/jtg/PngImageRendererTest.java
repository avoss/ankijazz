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

  @Test
  public void testAnkiImage() {
    Song song = MidiTestUtils.createRandomSong(ctx.getNumberOfBars());
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    Path path = Paths.get("build/PngImageRendererTest.png");
    renderer.renderTo(path);
    assertTrue(path.toFile().exists());
  }
  
  @Test
  public void testTwoChordsInOneBar() {
    Bar b1 = Bar.of(Chord.of(Scales.Cm7.transpose(Note.D), "Dm7"), Chord.of(Scales.C7.transpose(Note.G), "G7"));
    Bar b2 = Bar.of(Chord.of(Scales.Cmaj7, "CMA7"));
    List<Bar> bars = new ArrayList<>();
    IntStream.range(0, 8).forEach(i -> {
      bars.add(b1);
      bars.add(b2);
    });
    Song song = new Song(bars);
    PngImageRenderer renderer = new PngImageRenderer(ctx, song);
    Path path = Paths.get("build/PngImageRendererTest2.png");
    renderer.renderTo(path);
    assertTrue(path.toFile().exists());
  }

}
