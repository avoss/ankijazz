package de.jlab.scales.theory;

import static de.jlab.scales.theory.BuiltInScaleTypes.*;
import static de.jlab.scales.theory.Note.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BuiltInScaleTypesTest {

  @Test
  public void test() {
    assertEquals(C, Major.notationKey().apply(C));
    assertEquals(D, Major.notationKey().apply(D));
    assertEquals(Bb, MelodicMinor.notationKey().apply(C));
    assertEquals(C, MelodicMinor.notationKey().apply(D));
    assertEquals(Eb, HarmonicMinor.notationKey().apply(C));
    assertEquals(F, HarmonicMinor.notationKey().apply(D));
    assertEquals(C, Dominant7.notationKey().apply(G));
    
  }

}
