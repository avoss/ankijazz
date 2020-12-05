package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.Scales.allKeys;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BuiltInChordTypesTest {

  @Test
  public void findScalesContainingChord() {
    for (ScaleType chordType : BuiltInChordTypes.values()) {
      Scale chord = chordType.getPrototype();
      System.out.println(chordType.getTypeName());
      for (ScaleType scaleType : List.of(Major, MelodicMinor, HarmonicMajor)) {
        for (Scale scale : allKeys(scaleType.getPrototype())) {
          if (scale.asSet().containsAll(chord.asSet())) {
            KeySignature keySignature = ScaleUniverse.MODES.info(scale).getKeySignature();
            Note notationKey = keySignature.getNotationKey();
            System.out.println(String.format("  %s, Notation-Key: %s, accidentals: %d, Scale-Notes: %s", scaleType.getTypeName(), notationKey.getName(FLAT), keySignature.getNumberOfAccidentals(), scale));
          }
        }
      }
    }
  }

  @Test
  public void test() {
    assertEquals(Note.G, BuiltInChordTypes.Major7Sharp11.notationKey().apply(Note.C));
  }

}
