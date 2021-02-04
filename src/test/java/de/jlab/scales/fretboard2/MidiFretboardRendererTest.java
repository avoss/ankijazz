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
    Function<Note, Marker> markers = Marker.marker(Cmaj7);
    MidiFile mf = render(markers);
    mf.save(Paths.get("build").resolve("ForegroundAndBackground.midi"));
  }

  @Test
  public void testForegroundOnly() {
    MidiFile mf = render((n) -> Marker.FOREGROUND);
    mf.save(Paths.get("build").resolve("ForegroundOnly.midi"));
  }

  @Test
  public void testBackgroundOnly() {
    MidiFile mf = render((n) -> Marker.BACKGROUND);
    mf.save(Paths.get("build").resolve("BackgroundOnly.midi"));
  }
  
  private MidiFile render(Function<Note, Marker> markers) {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Fretboard fretboard = new Fretboard(position, markers);
    System.out.println(fretboard);

    MidiFretboardRenderer renderer = new MidiFretboardRenderer(fretboard, true, Cmaj7);
    Part part = renderer.render();
    MidiFile mf = new MidiFile();
    part.perform(mf);
    return mf;
  }

}
