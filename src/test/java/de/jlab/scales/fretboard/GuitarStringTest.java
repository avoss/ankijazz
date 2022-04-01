package de.jlab.scales.fretboard;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.G;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.jlab.scales.fretboard.GuitarString;
import de.jlab.scales.fretboard.Marker;

public class GuitarStringTest {

  @Test
  public void test() {
    GuitarString eString = new GuitarString(0, E);
    eString.mark(5, Marker.FOREGROUND);
    assertThat(eString.markerOf(1)).isEqualTo(Marker.EMPTY);
    assertThat(eString.markerOf(5)).isNotEqualTo(Marker.EMPTY);
    assertThat(eString.noteOf(0)).isEqualTo(E);
    assertThat(eString.fretOf(E)).isEqualTo(0);
    assertThat(eString.noteOf(5)).isEqualTo(A);
    assertThat(eString.fretOf(A)).isEqualTo(5);
    assertThat(eString.noteOf(8)).isEqualTo(C);
    assertThat(eString.fretOf(C)).isEqualTo(8);

    eString.mark(7, Marker.BACKGROUND);
    eString.mark(9, Marker.EMPTY);
    assertThat(eString.getMinFret().getAsInt()).isEqualTo(5);
    assertThat(eString.getMaxFret().getAsInt()).isEqualTo(7);
    
    GuitarString aString = new GuitarString(1, A);
    assertThat(aString.noteOf(7)).isEqualTo(E);
    assertThat(aString.getStringIndex()).isEqualTo(1);

  }

  @Test
  public void testEmptyString() {
    GuitarString gString = new GuitarString(0, G);
    assertThat(gString.getMinFret()).isEmpty();
  }
}
