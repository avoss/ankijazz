package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMajor;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.Note.*;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class BuiltinChordTypeTest {

  @Test
  public void findScalesContainingChord() {
    ScaleUniverse scales = new ScaleUniverse(false, List.of(Major, MelodicMinor, HarmonicMinor, HarmonicMajor));
    for (ScaleType chordType : BuiltinChordType.values()) {
      Scale chord = chordType.getPrototype();
      
      System.out.println(format("C%-5s is contained ", chordType.getTypeName()));
      for (ScaleInfo info : scales.findScalesContaining(chord.asSet())) {
        int position = 1 + info.getScale().indexOf(chord.getRoot());
        System.out.println(format("  at %d in %2s %s (%s)", position, info.getScale().getRoot().getName(FLAT), info.getTypeName(), info.getKeySignature().getAccidental().symbol()));
      }
    }
  }

  @Test
  public void testChords() {
    Scale dmaj7 = Scales.Cmaj7.transpose(D);
    assertEquals("D F# A C#", KeySignature.fromChord(dmaj7, D).toString(dmaj7));
    Scale dbm6 = Scales.Cm6.transpose(Db);
    assertEquals("Dbm6 Db E Ab Bb", KeySignature.fromChord(dbm6, B).toString(dbm6));
    // A#7#9 A# C# D E# G# 
  }
  
  @Test
  public void test() {
    assertEquals(Note.G, BuiltinChordType.Major7Sharp11.getKeySignature(Note.C).getNotationKey());
    //FIXME Abm7 Ab Cb Eb Gb 
  }

}
