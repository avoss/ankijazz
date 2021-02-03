package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

public class PngFretboardRendererTest {

  @Test
  public void testCLydian() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Function<Note, Marker> markers = Markers.marker(Cmaj7);
    assertEquals(4, position.getMaxFret() - position.getMinFret());
    Fretboard fretboard = new Fretboard(position, markers);
    FretboardRenderer<BufferedImage> renderer = new PngFretboardRenderer(fretboard);
    BufferedImage image = renderer.render();
    //Preview.preview(image);
    TestUtils.assertImageMatches(image, getClass(), "CLydian.png");
  }

  @Test
  public void testBoxOnly() {
    Fretboard fretboard = new Fretboard();
    Markers.box(fretboard, 5, Note.B, BoxMarker.BoxPosition.RIGHT, NPS.C_MINOR7_PENTATONIC);
    FretboardRenderer<BufferedImage> renderer = new PngFretboardRenderer(fretboard);
    BufferedImage image = renderer.render();
    //Preview.preview(image);
    TestUtils.assertImageMatches(image, getClass(), "BoxOnly.png");
  }
  
}
