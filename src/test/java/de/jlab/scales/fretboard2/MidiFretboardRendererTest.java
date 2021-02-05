package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.function.Function;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.fretboard2.MidiFretboardRenderer.MidiFretboardRendererBuilder;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Note;
import static org.assertj.core.api.Assertions.*;
public class MidiFretboardRendererTest {

  @Test
  public void testForegroundAndBackground() {
    Part part = builder(Marker.marker(Cmaj7))
      .renderForeground(true)
      .renderBackground(true)
      .foregroundIncludesRoot(true)
      .build()
      .render();
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundAndBackground.midi");
  }

  private MidiFretboardRendererBuilder builder(Function<Note, Marker> markers) {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Fretboard fretboard = new Fretboard(position, markers);
    return MidiFretboardRenderer.builder()
      .fretboard(fretboard)
      .backgroundChord(Cmaj7);
  }

  @Test
  public void testForegroundOnly() {
    Part part = builder((n) -> Marker.FOREGROUND)
        .renderForeground(true)
        .build()
        .render();
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundOnly.midi");
  }

  @Test
  public void testBackgroundOnly() {
    Part part = builder((n) -> Marker.BACKGROUND)
        .renderBackground(true)
        .build()
        .render();
    TestUtils.assertMidiMatches(part, getClass(), "BackgroundOnly.midi");
  }

  @Test
  public void testForegroundOnlyNoRoot() {
    Part part = builder(Marker.marker(Cmaj7))
        .renderForeground(true)
        .renderBackground(true)
        .build()
        .render();
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundAndBackgroundNoRoot.midi");
  }
  
}
