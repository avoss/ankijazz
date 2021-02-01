package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import org.junit.Test;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class PngFretboardRendererTest {
  /**
   * m7 pent
   * m6 pent
   * m7 bend3 pent
   */

  @Test
  public void testCLydian() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Function<Note, Marker> markers = n -> n == Cmaj7.getRoot() ? Markers.root() : (Cmaj7.contains(n) ? Markers.foreground() : Markers.background());
    assertEquals(4, position.getMaxFret() - position.getMinFret());
    Fretboard fretboard = new Fretboard(position, markers);
    FretboardRenderer<BufferedImage> renderer = new PngFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
    Preview.preview(renderer.render());
    
  }

}
