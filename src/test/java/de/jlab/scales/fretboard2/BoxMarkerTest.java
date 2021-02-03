package de.jlab.scales.fretboard2;

import static de.jlab.scales.fretboard2.BoxMarker.LeftRight.*;
import static de.jlab.scales.theory.Scales.CMinor7Pentatonic;
import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;
import org.mockito.Mockito;

import de.jlab.scales.theory.Note;
import static org.assertj.core.api.Assertions.*;

public class BoxMarkerTest {
  Fingering aMinorPent = NPS.C_MINOR7_PENTATONIC.transpose(Note.A);

  @Test
  public void testAminorPosition0Right() {
    Fretboard fretboard = new Fretboard();
    Position position = Markers.box(fretboard, 5, Note.A, RIGHT, aMinorPent);
    assertThat(position).isNotNull();
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(0));
    assertThat(fretboard.getMinFret()).isEqualTo(5);
    assertThat(fretboard.getMaxFret()).isEqualTo(5);
    Marker marker = fretboard.getMarker(5, 5);
    assertThat(marker.isEmpty()).isFalse();
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString highEString = fretboard.getString(5);
    marker.render(mock, highEString, Tuning.HIGH_E_STRING);
    Mockito.verify(mock).renderBox(highEString, 5, 5, 8);
  }

  @Test
  public void testAminorPosition0Left() {
    Fretboard fretboard = new Fretboard();
    BoxMarker marker = new BoxMarker(fretboard, Tuning.HIGH_E_STRING, Note.A, LEFT, aMinorPent);
    Position position = marker.mark();
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(4));
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString highEString = fretboard.getString(Tuning.HIGH_E_STRING);
    marker.render(mock, highEString, 5);
    Mockito.verify(mock).renderBox(highEString, 5, 2, 5);
  }

  @Test
  public void testRootOutsideOfPosition() {
    Fretboard fretboard = new Fretboard();
    BoxMarker marker = new BoxMarker(fretboard, Tuning.HIGH_E_STRING, Note.Bb, LEFT, aMinorPent);
    Position position = marker.mark();
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(4));
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString highEString = fretboard.getString(5);
    marker.render(mock, highEString, 6);
    Mockito.verify(mock).renderBox(highEString, 6, 2, 6);
  }

  @Test
  public void transposeRequired() {
    Fretboard fretboard = new Fretboard();
    BoxMarker marker = new BoxMarker(fretboard, Tuning.G_STRING, Note.A, LEFT, aMinorPent);
    Position position = marker.mark();
    assertThat(position).isEqualTo(aMinorPent.getPositions().get(3));
    MarkerRenderer mock = Mockito.mock(MarkerRenderer.class);
    GuitarString gString = fretboard.getString(Tuning.G_STRING);
    assertThat(gString.markerOf(14).isEmpty()).isFalse();
    marker.render(mock, gString, 14);
    Mockito.verify(mock).renderBox(gString, 14, 12, 15);
  }
  
}
