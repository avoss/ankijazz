package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
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
import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Test;

public class AccidentalTest {

  @Test
  public void testFromScale() {
      asList(C, G, D, A, E, B).forEach(root -> assertEquals("Sharp expected for key " + root, SHARP, fromScale(CMajor.transpose(root))));
      asList(F, Bb, Eb, Ab, Db, Gb).forEach(root -> assertEquals("Flat expected for key " + root, FLAT, fromScale(CMajor.transpose(root))));
  }

}
