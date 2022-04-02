package com.ankijazz.fretboard;

import static com.ankijazz.fretboard.BoxMarker.BoxPosition.LEFT;
import static com.ankijazz.fretboard.BoxMarker.BoxPosition.RIGHT;
import static com.ankijazz.fretboard.StandardTuning.A_STRING;
import static com.ankijazz.fretboard.StandardTuning.G_STRING;
import static com.ankijazz.fretboard.StandardTuning.HIGH_E_STRING;
import static com.ankijazz.fretboard.Tunings.STANDARD_TUNING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.Mockito;

import com.ankijazz.fretboard.Fingering;
import com.ankijazz.fretboard.Fretboard;
import com.ankijazz.fretboard.GuitarString;
import com.ankijazz.fretboard.Marker;
import com.ankijazz.fretboard.MarkerRenderer;
import com.ankijazz.fretboard.NPS;
import com.ankijazz.fretboard.Position;
import com.ankijazz.fretboard.BoxMarker.BoxPosition;
import com.ankijazz.fretboard.Fretboard.MarkedFret;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;

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
    // e.g. outline lydian chord
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
  public void testE7Sharp5Flat9RightLeft() {
    Fretboard fretboard = new Fretboard();
    Fingering fingering = NPS.C_MINOR6_PENTATONIC.transpose(Note.F);
    Position left = Marker.box(fretboard, A_STRING, Note.E, LEFT, fingering);
    Position right = Marker.box(fretboard, A_STRING, Note.E, RIGHT, fingering);
    assertEquals(left.toString(), "4 6|3 5|3 6|3 5|3 6|4 6");
    assertEquals(right.toString(), "8 10|8 11|8 10|7 10|9 11|8 10");
  }
  
  @Test
  public void transposeImprovesQuality() {
    Fretboard fretboard = new Fretboard();
    Position position = Marker.box(fretboard, HIGH_E_STRING, Note.F, RIGHT, NPS.C_MAJOR_CAGED);
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
        Fretboard fretboard1 = new Fretboard();
        Position position = Marker.box(fretboard1, stringIndex, root, boxPosition, fingering);
        List<Integer> markedFrets = fretboard1.getString(stringIndex).nonEmptyMarkerFrets().boxed().collect(Collectors.toList());
        assertEquals(String.format("%s %s\n%s", fingering, root.name(), fretboard1), 1, markedFrets.size());
        int markedFret = markedFrets.get(0);

        Fretboard fretboard2 = new Fretboard(position, Marker.outline(scale));
        assertEquals(String.format("%s %s\n%s", fingering, root.name(), fretboard2), Marker.ROOT, fretboard2.getString(stringIndex).markerOf(markedFret));
      }
    }
  }
  
  @Test
  public void assertRequestedRootIsIncludedInPositionOfAlteredCaged() {
    Fretboard frontBoard = new Fretboard();
    int stringNumber = 1;
    // Ab-Altered
    Position position = Marker.box(frontBoard, stringNumber, Note.Ab, BoxPosition.RIGHT, NPS.C_MELODIC_MINOR_CAGED.transpose(Note.A), Marker.ROOT);
    List<MarkedFret> markedFrets = frontBoard.findMarkedFrets(Fretboard.NON_EMPTY);
    assertThat(markedFrets.size()).isEqualTo(1);
    MarkedFret markedFret = markedFrets.get(0);
    assertThat(markedFret.getStringNumber()).isEqualTo(stringNumber);
    Collection<Integer> positionFrets = position.getFrets(stringNumber);
    assertThat(positionFrets).contains(markedFret.getFretNumber());
  }

  @Test
  public void assertRequestedRootIsIncludedInPositionOf3NPSMajor() {
    Fretboard fretBoard = new Fretboard();
    int stringNumber = 3;
    Position position = Marker.box(fretBoard, stringNumber, Note.C, BoxPosition.RIGHT, NPS.C_MAJOR_3NPS);
    List<MarkedFret> markedFrets = fretBoard.findMarkedFrets(Fretboard.NON_EMPTY);
    assertThat(markedFrets.size()).isEqualTo(1);
    MarkedFret markedFret = markedFrets.get(0);
    assertThat(markedFret.getStringNumber()).isEqualTo(stringNumber);
    Collection<Integer> positionFrets = position.getFrets(stringNumber);
    assertThat(positionFrets).contains(markedFret.getFretNumber());
  }

}
