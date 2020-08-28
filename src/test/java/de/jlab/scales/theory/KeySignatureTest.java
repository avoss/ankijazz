package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.KeySignature.fromScale;
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
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Splitter;

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
    Analyzer.Result r = new Analyzer.Result(accidental);
    r.getMajorNotesWithAccidental().addAll(asList(notesWithAccidental));
    r.initialize();
    assertEquals(expectedRoot, r.getRoot());
    assertEquals(expectedNumberOfAccidentalsInKeySignature, r.getNumberOfAccidentalsInKeySignature());
  }
  
  @Test
  public void testFromScale() {
    assertSignature("Major Scale", CMajor, SHARP, C, G, D, A, E, B, Gb);
    assertSignature("Major Scale", CMajor, FLAT, F, Bb, Eb, Ab, Db);
    assertSignature("Melodic Minor", CMelodicMinor, SHARP, D, G, A, E, B, Gb, Db);
    assertSignature("Melodic Minor", CMelodicMinor, FLAT, Ab, Eb, Bb, F, C);
    assertSignature("Harmonic Minor", CHarmonicMinor, SHARP, A, E, B, Gb, Db);
    assertSignature("Harmonic Minor", CHarmonicMinor, FLAT, Ab, Eb, Bb, F, C, D, G);
   
  }

  private void assertSignature(String scaleType, Scale cscale, Accidental expected, Note ... roots) {
    for (int i = 0; i < roots.length; i++) {
      Scale scale = cscale.transpose(roots[i]);
      KeySignature actual = fromScale(scale);
      System.out.println(format("%2s %15s Signature: %2s [%s]", roots[i].toString(), scaleType, actual, scale.asScale(actual.getAccidental())));
      String message = format("%s-%s expected %s [%s] actual %s [%s]:", roots[i].toString(), scaleType, expected, scale.asScale(expected), actual.getAccidental(), scale.asScale(actual.getAccidental()));
      assertEquals(message, expected, actual.getAccidental());
      assertNoDuplicateNotesExist(scale, actual);
    }
  }

  private void assertNoDuplicateNotesExist(Scale scale, KeySignature signature) {
    Set<String> set = new HashSet<>();
    String string = scale.asScale(signature.getAccidental());
    Splitter.on(" ").split(string).forEach(s -> set.add(s.replaceAll("#|b", "")));
    assertEquals(string, scale.length(), set.size());
  }

  

}
