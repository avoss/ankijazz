package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Accidental.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.Sets;

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
    assertChord("CDim", C, Eb, Gb);
    assertChord("CAug", C, E, Ab);
    assertChord("CSus2", C, D, G);
    assertChord("CSus4", C, F, G);
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
    assertChord("C79", C, E, G, Bb, D);
    assertChord("C7#9", C, E, G, Bb, Eb);
    assertChord("C711", C, E, G, Bb, F);
    assertChord("C713", C, E, G, Bb, A);
    assertChord("C7b13", "C7#5", C, E, G, Bb, Ab);
    assertChord("CDim7", C, Eb, Gb, A);
    
  }
  
  @Test
  public void testMajorChords() {
    assertChord("CMaj7", C, E, G, B);
    assertChord("CMaj7#11", "CMaj7b5", C, E, G, B, Gb);
  }

  @Test
  public void testMinorChords() {
    assertChord("Cm7", C, Eb, G, Bb);
    assertChord("Cm79", C, Eb, G, Bb, D);
    assertChord("Cm9", C, Eb, G, D);
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
    assertEquals("could not parse " + symbol, Sets.newTreeSet(Arrays.asList(notes)), chord.getNotes());
    assertEquals("could not print " + symbol, expected, new ChordParser(accidental).asChord(chord));
  }
}
