package com.ankijazz.theory;

import static com.ankijazz.theory.Accidental.DOUBLE_FLAT;
import static com.ankijazz.theory.Accidental.DOUBLE_SHARP;
import static com.ankijazz.theory.Accidental.FLAT;
import static com.ankijazz.theory.Accidental.SHARP;
import static com.ankijazz.theory.Accidental.fromOffset;
import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Ab;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
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
