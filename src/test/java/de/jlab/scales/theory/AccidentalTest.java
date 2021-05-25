package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Accidental.SHARP;
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
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AccidentalTest {

  @Test
  public void testNumberOfAccidentals() {
    Note[] sharps = {C, G, D, A, E, B, Gb, Db, Ab, Eb, Bb, F};
    for (int i = 0; i < sharps.length; i++)          {
      assertEquals(i, SHARP.numberOfAccidentals(sharps[i]));
    }
    Note[] flats = {C, F, Bb, Eb, Ab, Db, Gb, B, E, A, D, G};
    for (int i = 0; i < flats.length; i++)          {
      assertEquals(i, FLAT.numberOfAccidentals(flats[i]));
    }
  }
  
  @Test
  public void testOffsetToAccidental() {
    assertEquals(SHARP, fromOffset(1));
    assertEquals(FLAT, fromOffset(-1));
    assertEquals(DOUBLE_SHARP, fromOffset(2));
    assertEquals(DOUBLE_FLAT, fromOffset(-2));
  }

}
