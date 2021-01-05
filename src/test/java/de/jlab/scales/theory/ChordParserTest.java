package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
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
    assertChord("Csus2", C, D, G);
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
    assertChord("C7b13", "C7#5", C, E, Bb, Ab);
    assertChord("Cdim7", C, Eb, Gb, A);
    assertChord("C7sus4", C, F, G, Bb);
  }
  
  @Test
  public void testBuiltinChordTypes() {
    for (ScaleType type : BuiltinChordType.values()) {
      for (Note root : Note.values()) {
        Scale expected = type.getPrototype().transpose(root);
        for (KeySignature key : type.getKeySignatures(root)) {
          String symbol = key.notate(expected.getRoot()).concat(type.getTypeName());
          Scale actual = ChordParser.parseChord(symbol);
          assertEquals("could not parse " + symbol, expected, actual);
          /*
           * other chords use b5, only maj7#11 uses #11 instead. ChordParser will return maj7b5
           */
          if (type != BuiltinChordType.Major7Sharp11) {
            assertEquals("could not print " + symbol, symbol, new ChordParser(key.getAccidental()).asChord(actual));
          }
        }
      }
    }
  }
  
  @Test
  public void testMajorChords() {
    assertChord("CΔ7", C, E, G, B);
    assertChord("CΔ7#11", "CΔ7b5", C, E, B, Gb);
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
    assertEquals("could not print " + symbol, expected, new ChordParser(accidental).asChord(chord));
  }
}
