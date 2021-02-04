package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.function.Function;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Note;
import static org.assertj.core.api.Assertions.*;
public class MidiFretboardRendererTest {

  @Test
  public void testForegroundAndBackground() {
    Part part = render(Marker.marker(Cmaj7), true);
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundAndBackground.midi");
  }

  @Test
  public void testForegroundOnly() {
    Part part = render((n) -> Marker.FOREGROUND, true);
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundOnly.midi");
  }

  @Test
  public void testBackgroundOnly() {
    Part part = render((n) -> Marker.BACKGROUND, true);
    TestUtils.assertMidiMatches(part, getClass(), "BackgroundOnly.midi");
  }

  @Test
  public void testForegroundOnlyNoRoot() {
    Part part = render(Marker.marker(Cmaj7), false);
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundAndBackgroundNoRoot.midi");
  }
  
  private Part render(Function<Note, Marker> markers, boolean foregroundIncludesRoot) {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Fretboard fretboard = new Fretboard(position, markers);

    MidiFretboardRenderer renderer = new MidiFretboardRenderer(fretboard, foregroundIncludesRoot, Cmaj7);
    return renderer.render();
  }

}
