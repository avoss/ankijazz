package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.G;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class GuitarStringTest {

  @Test
  public void test() {
    GuitarString eString = new GuitarString(0, E);
    eString.mark(5, Markers.foreground());
    assertThat(eString.markerOf(1)).isEqualTo(Markers.empty());
    assertThat(eString.markerOf(5)).isNotEqualTo(Markers.empty());
    assertThat(eString.noteOf(0)).isEqualTo(E);
    assertThat(eString.noteOf(5)).isEqualTo(A);
    assertThat(eString.noteOf(8)).isEqualTo(C);

    eString.mark(7, Markers.background());
    eString.mark(9, Markers.empty());
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
