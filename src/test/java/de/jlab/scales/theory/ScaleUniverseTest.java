package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.DiminishedTriad;
import static de.jlab.scales.theory.BuiltInScaleTypes.*;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor7Pentatonic;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CdimTriad;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.allModes;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class ScaleUniverseTest {

  private static ScaleUniverse allScales = new ScaleUniverse(true);
  private static ScaleUniverse jazz = new ScaleUniverse(true, Major, MelodicMinor, HarmonicMinor);

  @Test
  public void testSuperScales() {
    ScaleUniverse universe = new ScaleUniverse(false, Major, Minor7Pentatonic);
    for (Scale scale : universe) {
      // System.out.println(scale.getName());
      ScaleInfo info = universe.info(scale);
      if (scale.getNotes().size() == 5) {
        assertEquals(3, info.getSuperScales().size());
        assertEquals(0, info.getSubScales().size());
      } else if (scale.getNotes().size() == 7) {
        assertEquals(0, info.getSuperScales().size());
        assertEquals(3, info.getSubScales().size());
      } else
        fail("invalid # notes");
    }
  }

  @Test
  public void testOrdering1() {
    ScaleUniverse universe = new ScaleUniverse(Major, MelodicMinor);
    List<Scale> expected = Scales.allKeys(asList(CMajor, CMelodicMinor));
    List<Scale> actual = Lists.newArrayList(universe.iterator());
    assertThat(expected).isEqualTo(actual);
    ScaleInfo info = universe.info(Cm7b5.transpose(B));
    assertThat(info.getSuperScales()).containsExactly(CMajor, CMelodicMinor, CMelodicMinor.transpose(D));
  }

  @Test
  public void testOrdering2() {
    ScaleUniverse universe = new ScaleUniverse(MelodicMinor, Major);
    List<Scale> expected = Scales.allKeys(asList(CMelodicMinor, CMajor));
    List<Scale> actual = Lists.newArrayList(universe.iterator());
    assertThat(expected).isEqualTo(actual);
    ScaleInfo info = universe.info(Cm7b5.transpose(B));
    assertThat(info.getSuperScales()).containsExactly(CMelodicMinor, CMelodicMinor.transpose(D), CMajor);
  }
  
  
  @Test
  public void testMatchingScalesForChordNotInUniverse() {
    ScaleUniverse universe = new ScaleUniverse(false, Major, MelodicMinor, DiminishedTriad);
    ScaleInfo info = universe.info(Cm7b5.transpose(B));
    assertThat(info.getSuperScales()).containsExactlyInAnyOrder(CMajor, CMelodicMinor, CMelodicMinor.transpose(D));
    assertThat(info.getSubScales()).containsExactlyInAnyOrder(CdimTriad.transpose(B));
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
    assertEquals(expectedName, jazz.info(scale).getDefaultName());
  }

  @Test
  public void testModes() {
    Scale bbmajor = CMajor.transpose(Bb);
    assertInfo(bbmajor, bbmajor, "Bb Major Scale", KeySignature.of(Bb, FLAT));
    Scale cdorian = bbmajor.superimpose(C);
    assertInfo(cdorian, bbmajor, "C Dorian", KeySignature.of(Bb, FLAT));
   
  }

  @Test
  public void testChordInversion() {
    assertInfo(Cmaj7, Cmaj7, "CΔ7", KeySignature.of(C, FLAT));
    assertInfo(Cmaj7.superimpose(E), Cmaj7, "CΔ7/E");
  }

  @Test
  public void testIdentity() {
    ScaleInfo info = allScales.info(C7sus4.transpose(2));
    assertSame(info.getScale(), info.getParent());
  }
  
  @Test
  public void testUnknownScale() {
    Scale s = new Scale(D, E, Gb);
    assertInfo(s, s, "D9", KeySignature.of(D, SHARP));
  }
  
  @Test
  public void testMultipleInfos() {
    Collection<ScaleInfo> infos = allScales.infos(Cm7b5);
    assertThat(infos.size()).isGreaterThan(1);
    assertInfo(Cm7b5, Cm7b5, "Cø");
  }
  
  @Test
  public void testWithoutModes() {
    ScaleUniverse inf = new ScaleUniverse(false, BuiltInScaleTypes.Major);
    Scale ddorian = CMajor.superimpose(D);
    assertThat(inf.info(ddorian).getDefaultName()).isNotEqualTo("D Dorian");
  }
  
  @Test
  public void testTypeName() {
    assertTypeName(CMajor.superimpose(D), "Dorian");
    assertTypeName(C7.superimpose(E), "7");  // TODO should be "Dominant 7"
    assertTypeName(new Scale(C, Db, D, Eb), "1 b2 2 b3");
  }

  @Test
  public void testMajorKeySignatures() {
    assertSignature(CMajor, C);
    asList(G, D, A, E, B).forEach(root -> assertSignature(CMajor.transpose(root), root, SHARP));
    asList(F, Bb, Eb, Ab, Db).forEach(root -> assertSignature(CMajor.transpose(root), root, FLAT));
    assertSignature(CMajor.transpose(Gb), Gb, FLAT);
  }
  
  @Test
  public void testMajorModeKeySignatures() {
    for (Scale scale : allKeys(CMajor)) {
      KeySignature expected = jazz.info(scale).getKeySignature();
      for (Scale mode : allModes(scale)) {
        KeySignature actual = jazz.info(mode).getKeySignature();
        assertThat(actual).isEqualTo(expected);
      }
    }
    Scale edorian = CMajor.transpose(D).superimpose(E);
    assertSignature(edorian, D, SHARP);
  }
  
  @Test
  public void testMinorScalesUseSignatureFromAeolean() {
    final Scale CNaturalMinor = CMajor.transpose(Eb).superimpose(C);
    for (Note root : Note.values()) {
      Scale naturalMinor = CNaturalMinor.transpose(root);
      KeySignature expected = jazz.info(naturalMinor).getKeySignature();
      Scale melodicMinor = CMelodicMinor.transpose(root);
      Scale harmonicMinor = CHarmonicMinor.transpose(root);
      allModes(harmonicMinor, melodicMinor).forEach(scale -> {
        ScaleInfo info = jazz.info(scale);
        assertEquals("KeySignature mismatch " + info, expected, info.getKeySignature());
      });
    }
  }

  private void assertSignature(Scale scale, Note root, Accidental accidental) {
    assertSignature(scale, root);
    assertEquals(accidental, jazz.info(scale).getKeySignature().getAccidental());
  }

  private void assertSignature(Scale scale, Note root) {
    assertEquals(root, jazz.info(scale).getKeySignature().getRoot());
  }

  private void assertTypeName(Scale scale, String expectedType) {
    ScaleInfo info = allScales.info(scale);
    assertThat(info.getModeName()).isEqualTo(expectedType);
  }
  
  private void assertInfo(Scale scale, Scale parent, String name, KeySignature keySignature) {
    assertInfo(scale, parent, name);
    ScaleInfo info = allScales.info(scale);
    assertThat(info.getKeySignature()).isEqualTo(keySignature);
  }

  private void assertInfo(Scale scale, Scale parent, String name) {
    ScaleInfo info = allScales.info(scale);
    assertThat(info.getDefaultName()).isEqualTo(name);
    assertEquals(info.getScale(), scale);
    assertEquals(info.getParent(), parent);
  }
}
