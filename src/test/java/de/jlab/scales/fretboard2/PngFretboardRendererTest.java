package de.jlab.scales.fretboard2;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import org.junit.Test;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class PngFretboardRendererTest {

  @Test
  public void test() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Scale gMinorPentatonic = Scales.CMinor7Pentatonic.transpose(Note.A);
    Function<Note, Marker> markers = n -> n == Note.A ? Markers.root() : (gMinorPentatonic.contains(n) ? Markers.foreground() : Markers.background());
    assertEquals(4, position.getMaxFret() - position.getMinFret());
    Fretboard fretboard = new Fretboard(position, markers);
    FretboardRenderer<BufferedImage> renderer = new PngFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
    Preview.preview(renderer.render());
    
  }

}
