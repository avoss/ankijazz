package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Note.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScalePrinterTest {

  @Test
  public void testAsScale() {
    ScalePrinter sp = new ScalePrinter();
    assertEquals("C D E F G A B", sp.asScale(CMajor));
    assertEquals("G A B C D E F#", sp.asScale(CMajor.transpose(G)));
    assertEquals("F G A Bb C D E", sp.asScale(CMajor.transpose(F)));
    assertEquals("F# G# A# B C# D# E#", sp.asScale(CMajor.transpose(Gb)));
    assertEquals("C C# D D# E F F#", sp.asScale(new Scale(C, Db, D, Eb, E, F, Gb)));
    
    
  }

}
