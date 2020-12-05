package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltinScaleType.*;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.Scales.allKeys;
import static java.lang.String.format;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BuiltinChordTypeTest {

  @Test
  public void findScalesContainingChord() {
    ScaleUniverse scales = new ScaleUniverse(false, List.of(Major, MelodicMinor, HarmonicMinor, HarmonicMajor));
    for (ScaleType chordType : BuiltinChordType.values()) {
      Scale chord = chordType.getPrototype();
      ScaleInfo info = scales.findScalesContaining(chord.asSet()).get(0);
      System.out.println(format("%5s is contained in %2s %s", chordType.getTypeName(), info.getScale().getRoot().getName(FLAT), info.getTypeName()));
    }
  }

  @Test
  public void test() {
    assertEquals(Note.G, BuiltinChordType.Major7Sharp11.notationKey().apply(Note.C));
  }

}
