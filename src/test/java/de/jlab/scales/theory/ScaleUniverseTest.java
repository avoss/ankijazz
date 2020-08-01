package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.DiminishedTriad;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor7Pentatonic;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CdimTriad;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class ScaleUniverseTest {

  private static ScaleUniverse universe = new ScaleUniverse(true);

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
    List<Scale> expected = Scales.allKeys(Arrays.asList(CMajor, CMelodicMinor));
    List<Scale> actual = Lists.newArrayList(universe.iterator());
    assertThat(expected).isEqualTo(actual);
    ScaleInfo info = universe.info(Cm7b5.transpose(B));
    assertThat(info.getSuperScales()).containsExactly(CMajor, CMelodicMinor, CMelodicMinor.transpose(D));
  }

  @Test
  public void testOrdering2() {
    ScaleUniverse universe = new ScaleUniverse(MelodicMinor, Major);
    List<Scale> expected = Scales.allKeys(Arrays.asList(CMelodicMinor, CMajor));
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
  public void testModes() {
    Scale bbmajor = CMajor.transpose(Bb);
    assertInfo(bbmajor, bbmajor, "Bb Major Scale", FLAT);
    Scale cdorian = bbmajor.superimpose(C);
    assertInfo(cdorian, bbmajor, "C Dorian", FLAT);
   
  }

  @Test
  public void testChordInversion() {
    assertInfo(Cmaj7, Cmaj7, "CΔ7", FLAT);
    assertInfo(Cmaj7.superimpose(E), Cmaj7, "CΔ7/E");
  }

  @Test
  public void testIdentity() {
    ScaleInfo info = universe.info(C7sus4.transpose(2));
    assertSame(info.getScale(), info.getParent());
  }
  
  @Test
  public void testUnknownScale() {
    Scale s = new Scale(D, E, Gb);
    assertInfo(s, s, "D9", SHARP);
  }
  
  @Test
  public void testMultipleInfos() {
    Collection<ScaleInfo> infos = universe.infos(Cm7b5);
    assertThat(infos.size()).isGreaterThan(1);
    assertInfo(Cm7b5, Cm7b5, "Cø");
  }
  
  @Test
  public void testWithoutModes() {
    ScaleUniverse inf = new ScaleUniverse(false, BuiltInScaleTypes.Major);
    Scale ddorian = CMajor.superimpose(D);
    assertThat(inf.info(ddorian).getName()).isNotEqualTo("D Dorian");
  }
  
  @Test
  public void testTypeName() {
    assertTypeName(CMajor.superimpose(D), "Dorian");
    assertTypeName(C7.superimpose(E), "7");  // TODO should be "Dominant 7"
    assertTypeName(new Scale(C, Db, D, Eb), "1 b2 2 b3");
  }

  private void assertTypeName(Scale scale, String expectedType) {
    ScaleInfo info = universe.info(scale);
    assertThat(info.getTypeName()).isEqualTo(expectedType);
  }
  
  private void assertInfo(Scale scale, Scale parent, String name, Accidental accidental) {
    assertInfo(scale, parent, name);
    ScaleInfo info = universe.info(scale);
    assertThat(info.getAccidental()).isSameAs(accidental);
  }

  private void assertInfo(Scale scale, Scale parent, String name) {
    ScaleInfo info = universe.info(scale);
    assertThat(info.getName()).isEqualTo(name);
    assertEquals(info.getScale(), scale);
    assertEquals(info.getParent(), parent);
  }
}
