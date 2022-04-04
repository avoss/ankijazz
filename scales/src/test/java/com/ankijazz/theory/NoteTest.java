package com.ankijazz.theory;

import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
public class NoteTest {

  @Test
  public void testIsSemitone() {
    assertTrue(B.isSemitone(C));
    assertTrue(C.isSemitone(B));
    assertTrue(D.isSemitone(Eb));
    assertTrue(Eb.isSemitone(D));
    assertFalse(C.isSemitone(D));
    assertFalse(E.isSemitone(D));
  }
  
  @Test
  public void testSemitones() {
    assertEquals(0, C.semitones(C));
    assertEquals(1, C.semitones(Db));
    assertEquals(7, C.semitones(G));
    assertEquals(11, C.semitones(B));
  }

  @Test
  public void testDistance() {
    assertEquals(0, C.distance(C));
    assertEquals(1, C.distance(Db));
    assertEquals(5, C.distance(G));
    assertEquals(6, C.distance(Gb));
    assertEquals(3, C.distance(A));
    assertEquals(1, C.distance(B));
  }

  @Test
  public void testTranspose() {
    assertEquals(C, C.transpose(12));
    assertEquals(C, C.transpose(-12));
    assertEquals(A, C.transpose(-3));
    assertEquals(Eb, C.transpose(3));
  }
}
