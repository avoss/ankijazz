package de.jlab.scales.theory;

import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static org.junit.Assert.assertEquals;

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
