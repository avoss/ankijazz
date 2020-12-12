package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.BuiltinChordType.*;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMajor;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.CMajor;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
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
  //@Ignore
  public void findMajorScalesContainingAllChords() {
    ScaleUniverse scales = new ScaleUniverse(false, List.of(Major));
    for (ScaleType chordType : BuiltinChordType.values()) {
      for (Scale chord : Scales.allKeys(chordType.getPrototype())) {
        System.out.println(format("%2s %-5s (%s/%s) is contained ", chord.getRoot().name(), chordType.getTypeName(), chord.asScale(SHARP), chord.asScale(FLAT)));
        for (ScaleInfo info : scales.findScalesContaining(chord.asSet())) {
          int position = 1 + info.getScale().indexOf(chord.getRoot());
          System.out.println(format("  at %d in %2s %s (%s)", position, info.getScale().getRoot().getName(FLAT), info.getTypeName(), info.getKeySignature().getAccidental().symbol()));
        }
      }
      
    }
  }

  @Test
  public void testSomeChords() {
    // E# rejected for chords
    assertNotation(Major7, Gb, /* "F# A# C# E#", */ "Gb Bb Db F");
    assertNotation(Major7, C, "C E G B");
    assertNotation(BuiltinChordType.Minor7, Note.Eb, "D# F# A# C#", "Eb Gb Bb Db");
  }


  private void assertNotation(ScaleType type, Note root, String ... expected) {
    Set<String> expectedSet = Arrays.stream(expected).collect(toSet());
    Set<String> actualSet = type.getKeySignatures(root).stream()
      .map(k -> k.toString(type.getPrototype().transpose(root)))
      .collect(toSet());
    assertEquals(expectedSet, actualSet);
  }
  
}
