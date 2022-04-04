package com.ankijazz.theory;

import static com.ankijazz.theory.BuiltinScaleType.HarmonicMinor;
import static com.ankijazz.theory.BuiltinScaleType.Major;
import static com.ankijazz.theory.BuiltinScaleType.MelodicMinor;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.Gb;
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
