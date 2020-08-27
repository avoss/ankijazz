package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Accidental.fromScale;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Test;

public class AccidentalTest {

  @Test
  public void testFromScale() {
 //   fromScale(CHarmonicMinor.transpose(Ab));
    assertAccidental("Major Scale", CMajor, SHARP, C, G, D, A, E, B, Gb);
    assertAccidental("Major Scale", CMajor, FLAT, F, Bb, Eb, Ab, Db);
    assertAccidental("Melodic Minor", CMelodicMinor, SHARP, D, G, A, E, B, Gb, Db);
    assertAccidental("Melodic Minor", CMelodicMinor, FLAT, Ab, Eb, Bb, F, C);
    assertAccidental("Harmonic Minor", CHarmonicMinor, SHARP, A, E, B, Gb, Db);
    assertAccidental("Harmonic Minor", CHarmonicMinor, FLAT, Ab, Eb, Bb, F, C, D, G);
   
  }

  private void assertAccidental(String scaleType, Scale cscale, Accidental expected, Note ... roots) {
    for (int i = 0; i < roots.length; i++) {
      Scale scale = cscale.transpose(roots[i]);
      Accidental actual = fromScale(scale);
      String rootName = roots[i].toString();
      System.out.println(String.format("%s-%s = %s (%s)", rootName, scaleType, scale.asScale(actual), actual));
      assertEquals(String.format("%s-%s expected=[%s] actual=[%s]:", rootName, scaleType, scale.asScale(expected), scale.asScale(actual)), expected, actual);
    }
  }

}
