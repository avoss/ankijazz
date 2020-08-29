package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
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

}
