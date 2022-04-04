package com.ankijazz.theory;

import static com.ankijazz.theory.Accidental.FLAT;
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
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class ChordParserTest {

  @Test
  public void testPowerChord() {
    assertChord("C5", C, G);
  }

  @Test
  public void testFlatsAndSharps() {
    assertChord("Bb6", "Bb6", FLAT, Bb, D, F, G);
    assertChord("A#6", "Bb6", FLAT, Bb, D, F, G);
    assertChord("C#6", "C#6", SHARP, Db, F, Ab, Bb);
    assertChord("Db6", "C#6", SHARP, Db, F, Ab, Bb);
  }
  
  @Test
  public void testTriads() {
    assertChord("C", C, E, G);
    assertChord("Cm", C, Eb, G);
    assertChord("Cdim", C, Eb, Gb);
    assertChord("Caug", C, E, Ab);
    assertChord("Csus4", C, F, G);
  }

  @Test
  public void testTriadInversions() {
    assertChord("C", E, C, G);
    assertChord("C", C, G, E);
  }
  
  @Test
  public void testDominantChords() {
    assertChord("C7", C, E, G, Bb);
    assertChord("C7b5", C, E, Gb, Bb);
    assertChord("C7#5", C, E, Ab, Bb);
    assertChord("C7b9", C, E, G, Bb, Db);
    assertChord("C7#9", C, E, G, Bb, Eb);
    assertChord("C9", C, E, G, Bb, D);
    assertChord("C11", C, E, G, Bb, F);
    assertChord("C13", C, E, G, Bb, A);
    assertChord("C7b13", C, E, G, Bb, Ab);
    assertChord("C7#11", C, E, G, Bb, Gb);
    assertChord("Cdim7", C, Eb, Gb, A);
    assertChord("C7sus4", C, F, G, Bb);
  }
  
  @Test
  public void assertThatAllBuiltinChordTypesAreParsedCorrectly() {
    for (ScaleType type : BuiltinChordType.values()) {
      for (Note root : Note.values()) {
        Scale expected = type.getPrototype().transpose(root);
        for (KeySignature key : type.getKeySignatures(root)) {
          String symbol = key.notate(expected.getRoot()).concat(type.getTypeName());
          Scale actual = ChordParser.parseChord(symbol);
          assertEquals("could not parse " + symbol, expected, actual);
        }
      }
    }
  }
  
  @Test
  public void testMajorChords() {
    assertChord("C6", C, E, G, A);
    assertChord("C69", C, E, G, A, D);
    assertChord("CΔ7", C, E, G, B);
    assertChord("Cmaj7", "CΔ7", C, E, G, B);
    assertChord("CΔ9", C, E, G, B, D);
    assertChord("Cmaj9", "CΔ9", C, E, G, B, D);
    assertChord("CΔ7#11", C, E, G, B, Gb);
  }

  @Test
  public void testMinorChords() {
    assertChord("Cm7", C, Eb, G, Bb);
    assertChord("Cm9", C, Eb, G, Bb, D);
    assertChord("Cm11", C, Eb, G, Bb, F);
    assertChord("Cm7b5", C, Eb, Gb, Bb);
  }
  
  private void assertChord(String symbol, Note... notes) {
    assertChord(symbol, symbol, FLAT, notes);
  }

  private void assertChord(String symbol, String expected, Note... notes) {
    assertChord(symbol, expected, FLAT, notes);
  }

  private void assertChord(String symbol, String expected, Accidental accidental, Note... notes) {
    Scale chord = ChordParser.parseChord(symbol);
    assertEquals("could not parse " + symbol, Set.of(notes), chord.asSet());
    assertEquals("could not print " + symbol, expected, ChordParser.asChord(chord, accidental));
  }
}
