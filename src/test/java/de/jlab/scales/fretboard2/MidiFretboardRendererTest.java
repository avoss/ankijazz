package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.function.Function;

import org.junit.Test;

import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Note;
import static org.assertj.core.api.Assertions.*;
public class MidiFretboardRendererTest {

  @Test
  public void testForegroundAndBackground() {
    MidiFile mf = render(Marker.marker(Cmaj7), true);
    mf.save(Paths.get("build").resolve("ForegroundAndBackground.midi"));
  }

  @Test
  public void testForegroundOnly() {
    MidiFile mf = render((n) -> Marker.FOREGROUND, true);
    mf.save(Paths.get("build").resolve("ForegroundOnly.midi"));
  }

  @Test
  public void testBackgroundOnly() {
    MidiFile mf = render((n) -> Marker.BACKGROUND, true);
    mf.save(Paths.get("build").resolve("BackgroundOnly.midi"));
  }

  @Test
  public void testForegroundOnlyNoRoot() {
    MidiFile mf = render(Marker.marker(Cmaj7), false);
    mf.save(Paths.get("build").resolve("ForegroundAndBackgroundNoRoot.midi"));
  }
  
  private MidiFile render(Function<Note, Marker> markers, boolean foregroundIncludesRoot) {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Fretboard fretboard = new Fretboard(position, markers);
    System.out.println(fretboard);
    System.out.println();

    MidiFretboardRenderer renderer = new MidiFretboardRenderer(fretboard, foregroundIncludesRoot, Cmaj7);
    Part part = renderer.render();
    MidiFile mf = new MidiFile();
    part.perform(mf);
    return mf;
  }

}
