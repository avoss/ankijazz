package de.jlab.scales.theory;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltinScaleType.DiminishedHalfWhole;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7sus4;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CHarmonicMajor;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CMinor6Pentatonic;
import static de.jlab.scales.theory.Scales.CWholeTone;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.allModes;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ScaleUniverseTest {

  private static ScaleUniverse allScales = new ScaleUniverse(true, ScaleUniverse.ALL_TYPES);
  private static ScaleUniverse jazz = new ScaleUniverse(true, List.of(Major, MelodicMinor, HarmonicMinor, DiminishedHalfWhole));

  class ScaleInfoComparator implements Comparator<ScaleInfo> {
    List<ScaleType> types = Arrays.asList(BuiltinScaleType.values());
    List<Note> roots = Arrays.asList(Note.values());
    @Override
    public int compare(ScaleInfo a, ScaleInfo b) {
      if (a.getParentInfo().getScaleType() != b.getParentInfo().getScaleType()) {
        return Integer.compare(types.indexOf(a.getParentInfo().getScaleType()), types.indexOf(b.getParentInfo().getScaleType()));
      }
      
      if (a.getParentInfo().getScale().getRoot() != b.getParentInfo().getScale().getRoot()) {
        return Integer.compare(roots.indexOf(a.getParentInfo().getScale().getRoot()), roots.indexOf(b.getParentInfo().getScale().getRoot()));
      }
      
      // Gb and F# have same root, but different notation
      String aRoot = a.getParentInfo().getKeySignature().notate(a.getParentInfo().getScale().getRoot());
      String bRoot = b.getParentInfo().getKeySignature().notate(b.getParentInfo().getScale().getRoot());
      if (!aRoot.equals(bRoot)) {
        return aRoot.compareTo(bRoot);
      }
      
      return Integer.compare(a.getModeIndex(), b.getModeIndex());
    }
  }

  @Test
  public void testChordTypes() {
    List<String> actual = new ArrayList<>();
    for (Scale chord : allKeys(Scales.allChords())) {
      for (ScaleInfo info : ScaleUniverse.CHORDS.infos(chord)) {
        String marker = TestUtils.reviewMarker(chord, info.getKeySignature());
        String line = String.format("%s %s %s", info.getScaleName(), info.getKeySignature().toString(chord), marker);
        actual.add(line);
      }
    }
    assertFileContentMatches(actual, ScaleUniverseTest.class, "testChordTypes.txt");
  }
    
  @Test
  public void testAllModesInAllKeys() {
    List<ScaleInfo> scaleInfos = allModes(allKeys(asList(CMajor, CMelodicMinor, CHarmonicMinor, CHarmonicMajor, CWholeTone, CDiminishedHalfWhole)))
      .stream()
      .flatMap(scale -> allScales.infos(scale).stream())
      .sorted(new ScaleInfoComparator())
      .collect(Collectors.toList());
    
    List<String> actual = new ArrayList<>();
    for (ScaleInfo scaleInfo: scaleInfos) {
      KeySignature signature = scaleInfo.getKeySignature();
      Scale scale = scaleInfo.getScale();
      String reviewMarker = TestUtils.reviewMarker(scale, signature);
      if (!scaleInfo.isInversion()) {
        actual.add("");
      }
      String message = format("%25s, Signature: %2s (%d%s), Notation: %s %s",
          scaleInfo.getScaleName(),
          signature.getKeySignatureString(),
          signature.getNumberOfAccidentals(),
          signature.getAccidental().symbol(), 
          signature.toString(scale),
          //scaleInfo.getParentInfo().getScaleName(),
          reviewMarker);
      actual.add(message);
    }
    assertFileContentMatches(actual, ScaleUniverseTest.class, "testAllModesInAllKeys.txt");
  }

  @Test
  public void testParents() {
    for (Scale parent : allKeys(asList(CMajor, CMelodicMinor, CHarmonicMinor, CHarmonicMajor))) {
      for (Scale mode : allModes(parent)) {
        ScaleInfo modeInfo = allScales.findFirstOrElseThrow(mode);
        assertEquals(parent, modeInfo.getParentInfo().getScale());
      }
    }
  }
  
  @Test
  public void testAbAltered() {
    ScaleInfo aMelodicMinor = allScales.findFirstOrElseThrow(CMelodicMinor.transpose(A));
    ScaleInfo aFlatAltered = allScales.findFirstOrElseThrow(aMelodicMinor.getScale().superimpose(Ab));
    assertEquals(aMelodicMinor.getKeySignature(), aFlatAltered.getKeySignature());
    assertEquals("G# Altered", aFlatAltered.getScaleName());
  }
  
  @Test
  public void testMinor6PentatonicModes() {
    assertEquals("C Minor6 Pentatonic", MODES.findFirstOrElseThrow(CMinor6Pentatonic).getScaleName());
    assertEquals("F Dominant9 Pentatonic", MODES.findFirstOrElseThrow(CMinor6Pentatonic.superimpose(F)).getScaleName());
    assertEquals("A Minor7b5 Pentatonic", MODES.findFirstOrElseThrow(CMinor6Pentatonic.superimpose(A)).getScaleName());
  }

  @Test
  public void testSuperScales() {
    ScaleUniverse universe = new ScaleUniverse(false, List.of(Major, Minor7Pentatonic));
    for (Scale scale : universe) {
      // System.out.println(scale.getName());
      ScaleInfo info = universe.findFirstOrElseThrow(scale);
      if (scale.asSet().size() == 5) {
        assertEquals(3, info.getSuperScales().size());
        assertEquals(0, info.getSubScales().size());
      } else if (scale.asSet().size() == 7) {
        assertEquals(0, info.getSuperScales().size());
        assertEquals(3, info.getSubScales().size());
      } else
        fail("invalid # notes");
    }
  }

  @Test
  public void testDorianTypeName() {
    Scale dorian = CMajor.superimpose(D);
    for (Note note: Note.values()) {
      Scale scale = dorian.transpose(note);
      assertEquals("Dorian", jazz.findFirstOrElseThrow(scale).getTypeName());
    }
  }
  
  @Test
  public void testDorianB2TypeName() {
    Scale dorian = CMelodicMinor.superimpose(D);
    for (Note note: Note.values()) {
      Scale scale = dorian.transpose(note);
      assertEquals("Dorian b2", jazz.findFirstOrElseThrow(scale).getTypeName());
    }
  }

  @Test
  public void testOrdering() {
    ScaleInfo info = allScales.findFirstOrElseThrow(Cm7b5.transpose(B));
    assertEquals(CMajor, info.getSuperScales().get(0));
  }

  @Test
  public void testTransposeScale() {
    Scale dmaj = CMajor.transpose(2);
    assertInfo(dmaj, dmaj, "D Major Scale");
  }
  
  @Test
  public void testNames() {
    assertName("C", CmajTriad);
    assertName("F#7", C7.transpose(Gb));
    assertName("Db7", C7.transpose(Db));
    
  }

  private void assertName(String expectedName, Scale scale) {
    assertEquals(expectedName, allScales.findFirstOrElseThrow(scale).getScaleName());
  }

  @Test
  public void testModes() {
    Scale bbmajor = CMajor.transpose(Bb);
    KeySignature signature = KeySignature.fromScale(bbmajor);
    assertInfo(bbmajor, bbmajor, "Bb Major Scale", signature);
    Scale cdorian = bbmajor.superimpose(C);
    assertInfo(cdorian, bbmajor, "C Dorian", signature);
   
  }

  @Test
  public void testChordInversion() {
    assertInfo(Cmaj7.superimpose(E), Cmaj7, "CΔ7/E");
  }

  @Test
  public void testIdentity() {
    ScaleInfo info = allScales.findFirstOrElseThrow(C7sus4.transpose(2));
    assertSame(info.getScale(), info.getParentInfo().getScale());
  }
  
  @Test
  public void testMultipleInfos() {
    Collection<ScaleInfo> infos = allScales.infos(Cm7b5);
    assertThat(infos.size()).isGreaterThan(1);
    assertInfo(Cm7b5, Cm7b5, "Cm7b5");
  }
  
  @Test
  public void testModeInUniverseWithoutModes() {
    ScaleUniverse universe = new ScaleUniverse(false, List.of(Major));
    Scale dDorianScale = CMajor.superimpose(D);
    List<ScaleInfo> info = universe.findScalesContaining(dDorianScale.asSet());
    assertEquals(1, info.size());
    assertThat(info.get(0).getScaleName()).isEqualTo("C Major Scale");
  }
  
  @Test
  public void testTypeName() {
    assertTypeName(CMajor.superimpose(D), "Dorian");
    assertTypeName(C7.superimpose(E), "7");
  }

  @Test
  public void testMajorModeKeySignatures() {
    for (Scale scale : allKeys(CMajor)) {
      KeySignature expected = jazz.findFirstOrElseThrow(scale).getKeySignature();
      for (Scale mode : allModes(scale)) {
        KeySignature actual = jazz.findFirstOrElseThrow(mode).getKeySignature();
        assertThat(actual).isEqualTo(expected);
      }
    }
    Scale edorian = CMajor.transpose(D).superimpose(E);
    assertSignature(edorian, D, SHARP);
  }
  
  @Test
  public void testBbAltered() {
    Scale gMelodicMinor = CMelodicMinor.transpose(G);
    Note majorKey = MelodicMinor.notationKey().apply(G);
    KeySignature expected = KeySignature.fromScale(gMelodicMinor, majorKey, Accidental.preferredAccidentalForMajorKey(majorKey));
    Scale gFlatAltered = gMelodicMinor.superimpose(Gb);
    ScaleInfo info = jazz.findFirstOrElseThrow(gFlatAltered);
    assertEquals(expected, info.getKeySignature());
  }

  private void assertSignature(Scale scale, Note root, Accidental accidental) {
    assertSignature(scale, root);
    assertEquals(scale.toString(), accidental, jazz.findFirstOrElseThrow(scale).getKeySignature().getAccidental());
  }

  private void assertSignature(Scale scale, Note root) {
    assertEquals(scale.toString(), root, jazz.findFirstOrElseThrow(scale).getKeySignature().getNotationKey());
  }

  private void assertTypeName(Scale scale, String expectedTypeName) {
    ScaleInfo info = allScales.findFirstOrElseThrow(scale);
    assertThat(info.getTypeName()).isEqualTo(expectedTypeName);
  }
  
  private void assertInfo(Scale scale, Scale parent, String name, KeySignature keySignature) {
    assertInfo(scale, parent, name);
    ScaleInfo info = allScales.findFirstOrElseThrow(scale);
    assertThat(info.getKeySignature()).isEqualTo(keySignature);
  }

  private void assertInfo(Scale scale, Scale parent, String name) {
    ScaleInfo info = allScales.findFirstOrElseThrow(scale);
    assertThat(info.getScaleName()).isEqualTo(name);
    assertEquals(info.getScale(), scale);
    assertEquals(info.getParentInfo().getScale(), parent);
  }
  
  @Test
  public void testFindScalesContaining() {
    assertScaleContaining(Scales.Cm7, "[Bb Major Scale, Eb Major Scale, Ab Major Scale, Bb Melodic Minor, G Harmonic Minor, C Diminished Half/Whole]");
    assertScaleContaining(Scales.Cm6, "[Bb Major Scale, C Melodic Minor, Bb Melodic Minor, E Harmonic Minor, G Harmonic Minor, C Diminished Half/Whole]");
    assertScaleContaining(Scales.Cdim7, "[E Harmonic Minor, G Harmonic Minor, Bb Harmonic Minor, C# Harmonic Minor, C Diminished Half/Whole, D Diminished Half/Whole]");
  }

  private void assertScaleContaining(Scale chord, String expected) {
    List<ScaleInfo> infos = jazz.findScalesContaining(chord.asSet());
    List<String> names = infos.stream().map(ScaleInfo::getScaleName).collect(Collectors.toList());
    assertEquals(expected, names.toString());
  }
}
