package de.jlab.scales.fretboard2;

import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.LEFT;
import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.RIGHT;
import static de.jlab.scales.fretboard2.StandardTuning.G_STRING;
import static de.jlab.scales.fretboard2.StandardTuning.HIGH_E_STRING;
import static de.jlab.scales.fretboard2.Tunings.STANDARD_TUNING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.Mockito;

import de.jlab.scales.fretboard2.BoxMarker.BoxPosition;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class BoxMarkerTest {
  Fingering aMinorPent = NPS.C_MINOR7_PENTATONIC.transpose(Note.A);

  @Test
  public void testAminorPosition0Right() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, HIGH_E_STRING, Note.A, RIGHT, aMinorPent);
    assertThat(position).isNotNull();
    assertEquals("5 8|5 7|5 7|5 7|5 8|5 8", position.toString());
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(0));
    assertThat(fretboard.getMinFret()).isEqualTo(5);
    assertThat(fretboard.getMaxFret()).isEqualTo(8);
    Marker marker = fretboard.getMarker(HIGH_E_STRING, 5);
    assertEquals(marker, Marker.ROOT);
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString highEString = fretboard.getString(5);
    marker.render(mock, highEString, HIGH_E_STRING);
    Mockito.verify(mock).renderRoot(highEString, 5);
  }

  @Test
  public void testAminorPosition0Left() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, HIGH_E_STRING, Note.A, LEFT, aMinorPent);
    assertEquals("3 5|3 5|2 5|2 5|3 5|3 5", position.toString());
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(4));
    assertThat(fretboard.getMinFret()).isEqualTo(2);
    assertThat(fretboard.getMaxFret()).isEqualTo(5);
    Marker marker = fretboard.getMarker(HIGH_E_STRING, 5);
    assertEquals(marker, Marker.ROOT);
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString highEString = fretboard.getString(HIGH_E_STRING);
    marker.render(mock, highEString, 5);
    Mockito.verify(mock).renderRoot(highEString, 5);
  }

  @Test
  public void testRootOutsideOfPosition() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, HIGH_E_STRING, Note.Bb, LEFT, aMinorPent);
    assertEquals("3 5|3 5|2 5|2 5|3 5|3 5", position.toString());
    assertThat(fretboard.getMinFret()).isEqualTo(2);
    assertThat(fretboard.getMaxFret()).isEqualTo(6);
    Marker marker = fretboard.getMarker(HIGH_E_STRING, 6);
    assertEquals(marker, Marker.ROOT);
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString highEString = fretboard.getString(5);
    marker.render(mock, highEString, 6);
    Mockito.verify(mock).renderRoot(highEString, 6);
  }

  @Test
  public void transposeRequired() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, G_STRING, Note.A, LEFT, aMinorPent);

    assertEquals("12 15|12 15|12 14|12 14|13 15|12 15", position.toString());
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(3));
    assertThat(fretboard.getMinFret()).isEqualTo(12);
    assertThat(fretboard.getMaxFret()).isEqualTo(15);

    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString gString = fretboard.getString(G_STRING);
    Marker marker = gString.markerOf(14);
    assertEquals(marker, Marker.ROOT);
    marker.render(mock, gString, 14);
    Mockito.verify(mock).renderRoot(gString, 14);
  }

  @Test
  public void transposeImprovesQuality() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, 5, Note.F, RIGHT, NPS.C_MAJOR_CAGED);
    assertEquals("12 13 15|12 14 15|12 14 15|12 14|12 13 15|12 13 15", position.toString());
  }

  @Test
  public void assertThatBoxNoteIsRootIfScaleIncludesRoot() {
    for (Note root : Note.values()) {
      for (Fingering fingering : NPS.allFingerings()) {
        assertThatBoxNoteIsRootIfScaleIncludesRoot(fingering.transpose(root));
      }
    }
  }

  private void assertThatBoxNoteIsRootIfScaleIncludesRoot(Fingering fingering) {
    Scale scale = fingering.getScale();
    Note root = scale.getRoot();

    for (int stringIndex = 0; stringIndex < STANDARD_TUNING.getStrings().size(); stringIndex++) {
      for (BoxPosition boxPosition : BoxPosition.values()) {
        Fretboard fretboard = new Fretboard();
        Position position = Marker.box(fretboard, stringIndex, root, boxPosition, fingering);
        List<Integer> markedFrets = fretboard.getString(stringIndex).nonEmptyMarkerFrets().boxed().collect(Collectors.toList());
        assertEquals(String.format("%s %s\n%s", fingering, root.name(), fretboard), 1, markedFrets.size());
        int markedFret = markedFrets.get(0);

        fretboard = new Fretboard(position, Marker.marker(scale));
        assertEquals(String.format("%s %s\n%s", fingering, root.name(), fretboard), Marker.ROOT, fretboard.getString(stringIndex).markerOf(markedFret));
      }
    }
  }

}
