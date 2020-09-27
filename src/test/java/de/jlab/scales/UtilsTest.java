package de.jlab.scales;

import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CWholeTone;
import static de.jlab.scales.theory.Scales.allModes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.jlab.scales.theory.Scale;

public class UtilsTest {


  @Test
  public void testUuid() {
    final int N = 1000;
    Set<String> set = new HashSet<>();
    for (int i = 0; i < N; i++) {
      set.add(Utils.uuid());
    }
    assertEquals(N, set.size());
    
  }

  @Test
  public void testSymmetricalDuplicate() {
    Scale scale = CDiminishedHalfWhole;
    assertFalse(Utils.isSymmetricalDuplicate(scale, scale));
    assertFalse(Utils.isSymmetricalDuplicate(scale, scale.superimpose(scale.getNote(1))));
    assertTrue(Utils.isSymmetricalDuplicate(scale, scale.superimpose(scale.getNote(2))));
    scale = CWholeTone.transpose(Gb);
    assertFalse(Utils.isSymmetricalDuplicate(scale, scale));
    assertTrue(Utils.isSymmetricalDuplicate(scale, scale.superimpose(scale.getNote(1))));
    scale = CMajor.transpose(F);
    for (Scale mode : allModes(scale)) {
      assertFalse(Utils.isSymmetricalDuplicate(scale, mode));
    }
  }
  
}
