package de.jlab.scales.fretboard2;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class FretboardTest {

  @Test
  public void testAmPentatonic() {
    Position position = NPS.C_MINOR7_PENTATONIC.transpose(Note.A).getPositions().get(0);
    Function<Note, Marker> markers = (n) -> Markers.foreground();
    Fretboard fretboard = new Fretboard(position, markers);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
    
    assertEquals(
        "|-o-|---|---|-o-|\n" + //
        "|-o-|---|---|-o-|\n" + //
        "|-o-|---|-o-|---|\n" + //
        "|-o-|---|-o-|---|\n" + //
        "|-o-|---|-o-|---|\n" + //
        "|-o-|---|---|-o-|" , //
        renderer.toString());
  }

  @Test
  public void testGMajorScaleWithAMinorPentatonic() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Scale aMinorPentatonic = Scales.CMinor7Pentatonic.transpose(Note.A);
    Function<Note, Marker> markers = Markers.marker(aMinorPentatonic);
    Fretboard fretboard = new Fretboard(position, markers);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
    
    assertEquals(
        "|---|-R-|---|-•-|-o-|\n" + //
        "|---|-o-|---|-•-|-o-|\n" + //
        "|-•-|-o-|---|-o-|---|\n" + //
        "|-•-|-o-|---|-R-|---|\n" + //
        "|---|-o-|---|-o-|---|\n" + //
        "|---|-R-|---|-•-|-o-|" , //
        renderer.toString());
  }
  
  @Test
  public void testAllScales() {
    List<Fingering> fingerings = List.of(
        NPS.C_MAJOR_CAGED, NPS.C_HARMONIC_MINOR_CAGED, NPS.C_MELODIC_MINOR_CAGED,
        NPS.C_MAJOR_3NPS, NPS.C_HARMONIC_MINOR_3NPS, NPS.C_MELODIC_MINOR_3NPS,
        NPS.C_MINOR7_PENTATONIC, NPS.C_MINOR6_PENTATONIC);
    for (Fingering fingering : fingerings) {
      List<String> actual = new ArrayList<>();
      for (Position position : fingering.getPositions()) {
        Scale scale = position.getScale();
        Function<Note, Marker> markers = Markers.marker(scale);
        Fretboard fretboard = new Fretboard(position, markers);
        StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard, position.getMinFret(), position.getMaxFret());
        List<String> rendered = renderer.render();
        actual.addAll(Stream.concat(rendered.stream(), Stream.of("--------------")).collect(toList()));
      }
      TestUtils.assertFileContentMatches(actual, getClass(), fingering.getName().concat(".txt"));
    }
  }
}
