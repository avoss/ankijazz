package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class ScalesTest {

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
    ScaleInfo info = Scales.info(C7sus4.transpose(2));
    assertThat(info.getScale()).isSameAs(info.getParent());
  }
  
  @Test
  public void testUnknownScale() {
    Scale s = new Scale(D, E, Gb);
    assertInfo(s, s, "D9", SHARP);
    ScaleInfo info = Scales.info(s);
    info.setName("Dadd9");
    assertInfo(s, s, "Dadd9", SHARP);
  }
  
  @Test
  public void testMultipleInfos() {
    Collection<ScaleInfo> infos = Scales.infos(Cm7b5);
    assertThat(infos.size()).isGreaterThan(1);
    assertInfo(Cm7b5, Cm7b5, "Cø");
  }
  
  private void assertInfo(Scale scale, Scale parent, String name, Accidental accidental) {
    assertInfo(scale, parent, name);
    ScaleInfo info = Scales.info(scale);
    assertThat(info.getAccidental()).isSameAs(accidental);
  }

  private void assertInfo(Scale scale, Scale parent, String name) {
    ScaleInfo info = Scales.info(scale);
    assertThat(info.getName()).isEqualTo(name);
    assertThat(info.getScale()).isEqualTo(scale);
    assertThat(info.getParent()).isEqualTo(parent);
  }

}
