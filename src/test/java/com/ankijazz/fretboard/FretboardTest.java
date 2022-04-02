package com.ankijazz.fretboard;

import static com.ankijazz.fretboard.Tunings.STANDARD_TUNING;
import static com.ankijazz.theory.Scales.CMinor7Pentatonic;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.fretboard.BoxMarker;
import com.ankijazz.fretboard.Fingering;
import com.ankijazz.fretboard.Fretboard;
import com.ankijazz.fretboard.Marker;
import com.ankijazz.fretboard.NPS;
import com.ankijazz.fretboard.Position;
import com.ankijazz.fretboard.StringFretboardRenderer;
import com.ankijazz.fretboard.Fretboard.Box;
import com.ankijazz.fretboard.Fretboard.MarkedFret;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.Scales;

public class FretboardTest {

  @Test
  public void testAmPentatonic() {
    Position position = NPS.C_MINOR7_PENTATONIC.transpose(Note.A).getPositions().get(0);
    Function<Note, Marker> markers = (n) -> Marker.FOREGROUND;;
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
  public void testToString() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Scale aMinorPentatonic = CMinor7Pentatonic.transpose(Note.A);
    Function<Note, Marker> markers = Marker.outline(aMinorPentatonic);
    Fretboard fretboard = new Fretboard(position, markers);
    fretboard.mark(0, 0, Marker.FOREGROUND);
    assertEquals(
        " |---|---|---|---|-R-|---|-•-|-o-|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" + //
        " |---|---|---|---|-o-|---|-•-|-o-|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" + //
        " |---|---|---|-•-|-o-|---|-o-|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" + //
        " |---|---|---|-•-|-o-|---|-R-|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" + //
        " |---|---|---|---|-o-|---|-o-|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" + //
        "o|---|---|---|---|-R-|---|-•-|-o-|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" , //
        
        fretboard.toString());
  }
  
  @Test
  public void testGMajorScaleWithAMinorPentatonic() {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Scale aMinorPentatonic = CMinor7Pentatonic.transpose(Note.A);
    Function<Note, Marker> markers = Marker.outline(aMinorPentatonic);
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
    Fretboard fretboard = new Fretboard(STANDARD_TUNING);
    fretboard.mark(0, 5, Marker.FOREGROUND);
    fretboard.mark(2, 7, Marker.BACKGROUND);
    fretboard.mark(3, 9, Marker.EMPTY);
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
  public void testQuestion() {
    Fretboard fretboard = new Fretboard();
    Marker.box(fretboard, 5, Note.B, BoxMarker.BoxPosition.RIGHT, NPS.C_MINOR7_PENTATONIC);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
    assertEquals(
        "|-R-|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|" , //
        renderer.toString());
  }

  @Test
  public void testAnswer() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, 5, Note.B, BoxMarker.BoxPosition.RIGHT, NPS.C_MINOR7_PENTATONIC);
    fretboard.mark(position, Marker.FOREGROUND);
    fretboard.markVisible(Note.B, Marker.ROOT);
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
    assertEquals(
        "|-R-|-o-|---|---|-o-|\n" + //
        "|---|-o-|---|---|-o-|\n" + //
        "|---|-o-|---|-o-|---|\n" + //
        "|---|-o-|-R-|-o-|---|\n" + //
        "|---|-o-|---|-o-|---|\n" + //
        "|-R-|-o-|---|---|-o-|" , //
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
        Function<Note, Marker> markers = Marker.outline(scale);
        Fretboard fretboard = new Fretboard(position, markers);
        StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
        List<String> rendered = renderer.render();
        actual.addAll(Stream.concat(rendered.stream(), Stream.of("--------------")).collect(toList()));
      }
      TestUtils.assertFileContentMatches(actual, getClass(), fingering.getName().concat(".txt"));
    }
  }
  
  @Test
  public void testBoxOnly() {
    Fretboard fretboard = new Fretboard();
    fretboard.setBox(new Box(5, 7));
    assertThat(fretboard.getMinFret()).isEqualTo(5);
    assertThat(fretboard.getMaxFret()).isEqualTo(7);
  }
  
  @Test
  public void testClearMark() {
    Position position = NPS.C_MINOR7_PENTATONIC.getPositions().get(0);
    Fretboard fretboard = new Fretboard(position, Marker.outline(Scales.CMinor7Pentatonic));
    List<MarkedFret> roots = fretboard.findMarkedFrets(Fretboard.ROOTS_ONLY);
    fretboard.clear();
    for (MarkedFret fret : roots) {
      fretboard.mark(fret);
    }
    
    StringFretboardRenderer renderer = new StringFretboardRenderer(fretboard);
    
    assertEquals(
        "|-R-|---|---|\n" + //
        "|---|---|---|\n" + //
        "|---|---|---|\n" + //
        "|---|---|-R-|\n" + //
        "|---|---|---|\n" + //
        "|-R-|---|---|" , //
        renderer.toString());
  }
}
