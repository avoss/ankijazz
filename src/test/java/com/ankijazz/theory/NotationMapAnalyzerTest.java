package com.ankijazz.theory;

import static com.ankijazz.theory.Accidental.FLAT;
import static com.ankijazz.theory.Accidental.NONE;
import static com.ankijazz.theory.Accidental.SHARP;
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
import static com.ankijazz.theory.Scales.CMajor;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.ankijazz.theory.NotationMapAnalyzer.Result;

public class NotationMapAnalyzerTest {

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
    Result result = new NotationMapAnalyzer().analyzeScale(CMajor.transpose(Note.E), SHARP);
    Map<Note, Accidental> map = result.getAccidentalMap();
    assertEquals(NONE, map.get(E));
    assertEquals(SHARP, map.get(Gb));
  }
  
}
