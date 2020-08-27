package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Note.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.theory.KeySignature.Analyzer.Result;

public class KeySignatureTest {

  @Test
  public void testResult() {
    assertResult(C, SHARP, 0);
    assertResult(G, SHARP, 1, F);
    
    assertResult(D, SHARP, 2, F, C);
    assertResult(D, SHARP, 2, C, F);
    assertResult(D, SHARP, 2, F, C, D);
    
    assertResult(A, SHARP, 3, F, C, G);
    assertResult(A, SHARP, 3, F, C, G, A, E);
    assertResult(A, SHARP, 3, A, C, G, F, E);
    
    assertResult(E, SHARP, 4, F, C, G, D);
    assertResult(E, SHARP, 4, F, C, G, D);
    assertResult(B, SHARP, 5, F, C, G, D, A);
    assertResult(Gb, SHARP, 6, F, C, G, D, A, E);
    
    assertResult(F, FLAT, 1, B);
    assertResult(F, FLAT, 1, B, D);
    assertResult(Bb, FLAT, 2, B, E);
    assertResult(Eb, FLAT, 3, B, E, A);
    assertResult(Ab, FLAT, 4, B, E, A, D);
    assertResult(Db, FLAT, 5, B, E, A, D, G);
    assertResult(Gb, FLAT, 6, B, E, A, D, G, C);
    
  }

  private void assertResult(Note expectedRoot, Accidental accidental, int expectedNumberOfAccidentalsInKeySignature, Note ...notesWithAccidental) {
    Result r = new Result(accidental);
    r.getNotesWithAccidental().addAll(asList(notesWithAccidental));
    r.initialize();
    assertEquals(expectedRoot, r.getRoot());
    assertEquals(expectedNumberOfAccidentalsInKeySignature, r.getNumberOfAccidentalsInKeySignature());
  }

}
