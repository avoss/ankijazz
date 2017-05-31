package de.jlab.scales.theory;

import static org.junit.Assert.*;

import org.junit.Test;
import static de.jlab.scales.theory.Note.*;
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
