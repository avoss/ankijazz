package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;

import static org.assertj.core.api.Assertions.*;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltinChordType.AugmentedTriad;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMajor;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.*;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.Gb;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

public class BuiltinChordTypeTest {
  
  @Test
  //@Ignore
  public void findScalesContainingChord() {
    ScaleUniverse scales = new ScaleUniverse(false, List.of(Major, MelodicMinor, HarmonicMinor, HarmonicMajor, DiminishedHalfWhole, WholeTone));
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
  public void findScalesContainingAllChords() {
    ScaleUniverse scales = new ScaleUniverse(false, List.of(Major, MelodicMinor, HarmonicMinor, HarmonicMajor));
    for (ScaleType chordType : BuiltinChordType.values()) {
      for (Scale chord : Scales.allKeys(chordType.getPrototype())) {
        System.out.println(format("%2s%-5s (%s/%s) is contained ", chord.getRoot().name(), chordType.getTypeName(), chord.asScale(SHARP), chord.asScale(FLAT)));
        for (ScaleInfo info : scales.findScalesContaining(chord.asSet())) {
          int position = 1 + info.getScale().indexOf(chord.getRoot());
          System.out.println(format("  at %d in %s (%s)", position, info.getScaleName(), info.getKeySignature().toString(info.getScale())));
        }
      }
      
    }
  }

  @Test
  public void testSomeChords() {
    assertNotation(Major7, Gb, "F# A# C# E#", "Gb Bb Db F");
    assertNotation(Major7, C, "C E G B");
    assertNotation(AugmentedTriad, F, "F A C#");
    assertNotation(BuiltinChordType.Minor7, Note.Eb, "D# F# A# C#", "Eb Gb Bb Db");
  }


  private void assertNotation(ScaleType type, Note root, String ... expected) {
    Set<String> expectedSet = Arrays.stream(expected).collect(toSet());
    Set<String> actualSet = type.getKeySignatures(root).stream()
      .map(k -> k.toString(type.getPrototype().transpose(root)))
      .collect(toSet());
    assertEquals(expectedSet, actualSet);
  }
  
  @Test
  public void assertAllChordsHaveBuiltinChordType() {
    for (Scale chord : Scales.allChords()) {
      Arrays.stream(BuiltinChordType.values()).filter(type -> type.getPrototype().equals(chord)).findAny().orElseThrow(() -> new NoSuchElementException(chord.asChord()));
    }
  }
  
  @Test
  public void assertNoDuplicateTypes() {
    Set<Scale> scales = Arrays.stream(BuiltinChordType.values()).map(t -> t.getPrototype()).collect(toSet());
    assertEquals(BuiltinChordType.values().length, scales.size());
    Set<String> names = Arrays.stream(BuiltinChordType.values()).map(t -> t.getTypeName()).collect(toSet());
    assertEquals(BuiltinChordType.values().length, names.size());
  }
  
  @Test
  public void testCLydian() {
    KeySignature gmajor = Major.getKeySignatures(Note.G).iterator().next();
    assertThat(gmajor.getAccidental()).isEqualTo(Accidental.SHARP);
    assertThat(gmajor.getNumberOfAccidentals()).isEqualTo(1);
    KeySignature actual = BuiltinChordType.Major7Sharp11.getKeySignatures(Note.C).iterator().next();
    assertThat(actual).isEqualTo(gmajor);
  }
  
  @Test
  public void testALydian() {
    KeySignature emajor = Major.getKeySignatures(Note.E).iterator().next();
    assertThat(emajor.getAccidental()).isEqualTo(Accidental.SHARP);
    assertThat(emajor.getNumberOfAccidentals()).isEqualTo(4);
    KeySignature actual = BuiltinChordType.Major7Sharp11.getKeySignatures(Note.A).iterator().next();
    assertThat(actual).isEqualTo(emajor);
  }
  
  @Test
  public void testE7sharp5flat9() {
    Set<KeySignature> expectedKeySignatures = BuiltinScaleType.HarmonicMinor.getKeySignatures(Note.A);
    assertThat(expectedKeySignatures.size()).isEqualTo(1);
    Set<KeySignature> actualKeySignatures = BuiltinChordType.Dominant7sharp5flat9.getKeySignatures(Note.E);
    assertThat(actualKeySignatures).isEqualTo(expectedKeySignatures);
  }
  
  @Test
  public void assertContextContainsChord() {
    for (BuiltinChordType type : chordsContainedInScale()) {
      Scale context = type.getContextScaleType().getPrototype().transpose(type.getContextScaleRoot());
      assertThat(context.asSet()).describedAs("Type: %s ", type.getTypeName()).containsAll(type.getPrototype().asSet());
    }
  }

  private Collection<BuiltinChordType> chordsContainedInScale() {
    Set<BuiltinChordType> invalid = Set.of(BuiltinChordType.Dominant7flat9sharp11, BuiltinChordType.Dominant7sharp9sharp11);
    return BuiltinChordType.stream().filter(t -> !invalid.contains(t)).collect(toSet());
  }

  @Test
  public void assertMostSimpleContextIsUsed() {
    for (BuiltinChordType type : chordsContainedInScale()) {
      Set<ScaleType> contextTypes = ScaleUniverse.SCALES.findScalesContaining(type.getPrototype().asSet()).stream().map(info -> info.getScaleType()).collect(toSet());
      boolean found = assertContextType(type, contextTypes, Major) || assertContextType(type, contextTypes, HarmonicMinor) || assertContextType(type, contextTypes, MelodicMinor) || assertContextType(type, contextTypes, HarmonicMajor);
      assertThat(found).describedAs(type.getTypeName()).isTrue();
    }
      
  }

  private boolean assertContextType(BuiltinChordType type, Set<ScaleType> contextTypes, BuiltinScaleType context) {
    if (contextTypes.contains((ScaleType) context)) {
      if (type.getContextScaleType() != context) {
        fail(format("%s should use context type %s", type.getTypeName(), context.getTypeName()));
      }
      return true;
    }
    return false;
  }

  
}
