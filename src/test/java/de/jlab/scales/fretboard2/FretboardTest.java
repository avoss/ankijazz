package de.jlab.scales.fretboard2;

import static org.junit.Assert.*;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class FretboardTest {

  @Test
  public void testAmPentatonic() {
    Position position = NPS.C_MINOR7_PENTATONIC.transpose(Note.A).getPositions().get(0);
    Function<Note, Marker> markers = (n) -> Markers.foreground();
    Fretboard fretboard = new Fretboard(position, markers);
    FretboardRenderer<String> renderer = new StringFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
    
    assertEquals(
        "|-o-|---|---|-o-|\n" + //
        "|-o-|---|---|-o-|\n" + //
        "|-o-|---|-o-|---|\n" + //
        "|-o-|---|-o-|---|\n" + //
        "|-o-|---|-o-|---|\n" + //
        "|-o-|---|---|-o-|" , //
        renderer.render());
  }

  @Test
  public void testGMajorScaleWithAMinorPentatonic() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Scale gMinorPentatonic = Scales.CMinor7Pentatonic.transpose(Note.A);
    Function<Note, Marker> markers = n -> n == Note.A ? Markers.root() : (gMinorPentatonic.contains(n) ? Markers.foreground() : Markers.background());
    Fretboard fretboard = new Fretboard(position, markers);
    FretboardRenderer<String> renderer = new StringFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
    
    assertEquals(
        "|---|-R-|---|-•-|-o-|\n" + //
        "|---|-o-|---|-•-|-o-|\n" + //
        "|-•-|-o-|---|-o-|---|\n" + //
        "|-•-|-o-|---|-R-|---|\n" + //
        "|---|-o-|---|-o-|---|\n" + //
        "|---|-R-|---|-•-|-o-|" , //
        renderer.render());
  }
  
}
