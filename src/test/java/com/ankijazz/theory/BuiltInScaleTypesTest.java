package de.jlab.scales.theory;

import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.Gb;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class BuiltInScaleTypesTest {

  @Test
  public void testNotationKey() {
    assertEquals(C, Major.notationKey().apply(C));
    assertEquals(D, Major.notationKey().apply(D));
    assertEquals(Bb, MelodicMinor.notationKey().apply(C));
    assertEquals(C, MelodicMinor.notationKey().apply(D));
    assertEquals(Eb, HarmonicMinor.notationKey().apply(C));
    assertEquals(F, HarmonicMinor.notationKey().apply(D));
  }
  
  @Test
  public void testKeySignature() {
    Set<KeySignature> keySignatures = Major.getKeySignatures(Gb);
    Set<String> rootNotations = keySignatures.stream().map(k -> k.notate(Gb)).collect(Collectors.toSet());
    assertThat(rootNotations).containsExactlyInAnyOrder("F#", "Gb");
  }

}
