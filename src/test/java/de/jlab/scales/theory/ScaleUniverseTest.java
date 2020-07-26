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
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.C7sus4;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CdimTriad;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

public class ScaleUniverseTest {

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
  public void testMatchingScalesForChordNotInUniverse() {
    ScaleUniverse universe = new ScaleUniverse(false, Major, MelodicMinor, DiminishedTriad);
    ScaleInfo info = universe.info(Cm7b5.transpose(B));
    assertThat(info.getSuperScales()).containsExactlyInAnyOrder(CMajor, CMelodicMinor, CMelodicMinor.transpose(D));
    assertThat(info.getSubScales()).containsExactlyInAnyOrder(CdimTriad.transpose(B));
  }

  ScaleUniverse scaleUniverse = new ScaleUniverse(true);

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
    assertInfo(Cmaj7, Cmaj7, "CΔ7", SHARP);
    assertInfo(Cmaj7.superimpose(E), Cmaj7, "CΔ7/E");
  }

  @Test
  public void testIdentity() {
    ScaleInfo info = scaleUniverse.info(C7sus4.transpose(2));
    assertSame(info.getScale(), info.getParent());
  }
  
  @Test
  public void testUnknownScale() {
    Scale s = new Scale(D, E, Gb);
    assertInfo(s, s, "D9", SHARP);
    ScaleInfo info = scaleUniverse.info(s);
  }
  
  @Test
  public void testMultipleInfos() {
    Collection<ScaleInfo> infos = scaleUniverse.infos(Cm7b5);
    assertThat(infos.size()).isGreaterThan(1);
    assertInfo(Cm7b5, Cm7b5, "Cø");
  }
  
  @Test
  public void testWithoutModes() {
    ScaleUniverse inf = new ScaleUniverse(false, BuiltInScaleTypes.Major);
    Scale ddorian = Scales.CMajor.superimpose(D);
    assertThat(inf.info(ddorian).getName()).isNotEqualTo("D Dorian");
  }
  
  private void assertInfo(Scale scale, Scale parent, String name, Accidental accidental) {
    assertInfo(scale, parent, name);
    ScaleInfo info = scaleUniverse.info(scale);
    assertThat(info.getAccidental()).isSameAs(accidental);
  }

  private void assertInfo(Scale scale, Scale parent, String name) {
    ScaleInfo info = scaleUniverse.info(scale);
    assertThat(info.getName()).isEqualTo(name);
    assertEquals(info.getScale(), scale);
    assertEquals(info.getParent(), parent);
  }
}
