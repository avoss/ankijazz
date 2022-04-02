package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.C13;
import static de.jlab.scales.theory.Scales.C6;
import static de.jlab.scales.theory.Scales.C69;
import static de.jlab.scales.theory.Scales.C7flat13;
import static de.jlab.scales.theory.Scales.C7flat5;
import static de.jlab.scales.theory.Scales.C7flat5sharp9;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.C7sharp11;
import static de.jlab.scales.theory.Scales.C7sharp5;
import static de.jlab.scales.theory.Scales.C9;
import static de.jlab.scales.theory.Scales.Cdim7;
import static de.jlab.scales.theory.Scales.CdimTriad;
import static de.jlab.scales.theory.Scales.Cm11;
import static de.jlab.scales.theory.Scales.Cm9;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.allModes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class StackerTest {

  @Test
  public void testStackedThirdsSomeChords() {
    assertStack(List.of(C, Eb, Gb), CdimTriad);
    assertStack(List.of(C, E, G), CmajTriad);
    assertStack(List.of(C, Eb, Gb, A), Cdim7);
    assertStack(List.of(C, E, G, A), C6);
    assertStack(List.of(C, E, G, A, D), C69);
    assertStack(List.of(C, E, G, Bb, D), C9);
    assertStack(List.of(C, Eb, G, Bb, D), Cm9);
    assertStack(List.of(C, Eb, G, Bb, F), Cm11);
    assertStack(List.of(C, E, G, Bb, A), C13);
    assertStack(List.of(D, Gb, A, C, Eb), C7flat9.transpose(D));
    assertStack(List.of(Ab, C, D, Gb, B), C7flat5sharp9.transpose(Ab));
    assertStack(List.of(C, E, Gb, Bb), C7flat5);
    assertStack(List.of(C, E, Ab, Bb), C7sharp5);
    assertStack(List.of(C, E, G, Bb, Gb), C7sharp11);
    assertStack(List.of(C, E, G, Bb, Ab), C7flat13);
  }
  
  private void assertStack(List<Note> expected, Scale scale) {
    assertEquals(expected, new Stacker(scale).getStackedThirds());
  }

  @Test
  public void testDistance() {
    for (ScaleType type : BuiltinChordType.values()) {
      Scale prototype = type.getPrototype();
      for (Scale scale : allModes(allKeys(prototype))) {
        Note[] thirds = new Stacker(scale).getStackedThirds().toArray(new Note[0]);
        for (int i = 1; i < thirds.length; i++) {
          assertThat(thirds[i-1].distance(thirds[i])).isLessThan(7);
        }
      }
    }
  }
  
  @Test
  public void testDegrees() {
    for (BuiltinChordType type : BuiltinChordType.values()) {
      Scale prototype = type.getPrototype();
      for (Scale scale : allKeys(prototype)) {
        List<Note> expected = type.getDegrees().asList(scale.getRoot());
        List<Note> actual = new Stacker(scale).getStackedThirds();
        assertEquals(expected, actual);
      }
    }
  }

}
