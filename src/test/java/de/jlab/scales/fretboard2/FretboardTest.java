package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Scales.CMinor7Pentatonic;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class FretboardTest {

  @Test
  public void testAmPentatonic() {
    Position position = NPS.C_MINOR7_PENTATONIC.transpose(Note.A).getPositions().get(0);
    Function<Note, Marker> markers = (n) -> Markers.foreground();
    Fretboard fretboard = new Fretboard(position, markers);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    assertThat(fretboard.getMinFret()).isEqualTo(position.getMinFret());
    assertThat(fretboard.getMaxFret()).isEqualTo(position.getMaxFret());
    
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
    Scale aMinorPentatonic = CMinor7Pentatonic.transpose(Note.A);
    Function<Note, Marker> markers = Markers.marker(aMinorPentatonic);
    Fretboard fretboard = new Fretboard(position, markers);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
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
  public void testMarkersOnFretboard() {
    Fretboard fretboard = new Fretboard(Tuning.STANDARD_TUNING);
    fretboard.mark(0, 5, Markers.foreground());
    fretboard.mark(2, 7, Markers.background());
    fretboard.mark(3, 9, Markers.empty());
    assertThat(fretboard.getMinFret()).isEqualTo(5);
    assertThat(fretboard.getMaxFret()).isEqualTo(7);

    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
    assertEquals(
        "|---|---|---|\n" + //
        "|---|---|---|\n" + //
        "|---|---|---|\n" + //
        "|---|---|-•-|\n" + //
        "|---|---|---|\n" + //
        "|-o-|---|---|" , //
        renderer.toString());
    
  }
  
  @Test
  public void testBoxWithMarkers() {
    Fretboard fretboard = new Fretboard();
    Position position = Markers.box(fretboard, 5, Note.B, BoxMarker.BoxPosition.RIGHT, NPS.C_MINOR7_PENTATONIC);
    fretboard.mark(position, Markers.marker(position.getScale()));
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
    assertEquals(
        "|-B-|-R-|---|---|-o-|\n" + //
        "|---|-o-|---|---|-o-|\n" + //
        "|---|-o-|---|-o-|---|\n" + //
        "|---|-o-|---|-R-|---|\n" + //
        "|---|-o-|---|-o-|---|\n" + //
        "|---|-R-|---|---|-o-|" , //
        renderer.toString());
  }

  @Test
  public void testBoxWithoutMarkers() {
    Fretboard fretboard = new Fretboard();
    Markers.box(fretboard, 5, Note.B, BoxMarker.BoxPosition.RIGHT, NPS.C_MINOR7_PENTATONIC);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
    assertEquals(
        "|-B-|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|" , //
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
        StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
        List<String> rendered = renderer.render();
        actual.addAll(Stream.concat(rendered.stream(), Stream.of("--------------")).collect(toList()));
      }
      TestUtils.assertFileContentMatches(actual, getClass(), fingering.getName().concat(".txt"));
    }
  }
}
