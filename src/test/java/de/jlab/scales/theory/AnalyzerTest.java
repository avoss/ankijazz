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
import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import de.jlab.scales.theory.Analyzer.Result;

public class AnalyzerTest {

  @Test
  public void testResultInitialize() {
    assertResult(C, SHARP);
    assertResult(G, SHARP, F);

    assertResult(D, SHARP, F, C);
    assertResult(D, SHARP, C, F);
    assertResult(D, SHARP, F, C, D);

    assertResult(A, SHARP, F, C, G);
    assertResult(A, SHARP, F, C, G, A, E);
    assertResult(A, SHARP, A, C, G, F, E);

    assertResult(E, SHARP, F, C, G, D);
    assertResult(E, SHARP, F, C, G, D);
    assertResult(B, SHARP, F, C, G, D, A);
    assertResult(Gb, SHARP, F, C, G, D, A, E);

    assertResult(F, FLAT, B);
    assertResult(F, FLAT, B, D);
    assertResult(Bb, FLAT, B, E);
    assertResult(Eb, FLAT, B, E, A);
    assertResult(Ab, FLAT, B, E, A, D);
    assertResult(Db, FLAT, B, E, A, D, G);
    assertResult(Gb, FLAT, B, E, A, D, G, C);
  }

  private void assertResult(Note expectedRoot, Accidental accidental, Note... notesWithAccidental) {
    Result r = new Result(CMajor, accidental);
    r.getMajorNotesWithAccidental().addAll(asList(notesWithAccidental));
    r.initialize();
    assertEquals(expectedRoot, r.getNotationKey());
  }
  
  @Test
  public void testAccidentalMap() {
    Result result = new Analyzer().analyzeScale(CMajor.transpose(Note.E), SHARP);
    Map<Note, Accidental> map = result.getAccidentalMap();
    assertEquals(NONE, map.get(E));
    assertEquals(SHARP, map.get(Gb));
  }
  
  @Test
  public void assertBug() {
    Scale fsmaj = CMajor.transpose(Gb);
    Analyzer a = new Analyzer();
    Result result = a.analyzeScale(fsmaj, SHARP);
    assertEquals("F#", result.getNotationMap().get(Gb));
  }

}
