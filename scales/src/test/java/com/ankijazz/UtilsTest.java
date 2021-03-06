package com.ankijazz;

import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.Gb;
import static com.ankijazz.theory.Scales.CDiminishedHalfWhole;
import static com.ankijazz.theory.Scales.CMajor;
import static com.ankijazz.theory.Scales.CWholeTone;
import static com.ankijazz.theory.Scales.allModes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.ankijazz.Utils.Interpolator;
import com.ankijazz.theory.BuiltinChordType;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;

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
  
  @Test
  public void testInterpolator() {
    Interpolator interpolator = Utils.interpolator(5, 10, 20, 40);
    assertEquals(20, interpolator.apply(4));
    assertEquals(20, interpolator.apply(5));
    assertEquals(28, interpolator.apply(7));
    assertEquals(40, interpolator.apply(10));
    assertEquals(40, interpolator.apply(20));
  }

  @Test
  public void testChordName() {
    assertEquals("Eb7", Utils.chordName(BuiltinChordType.Dominant7, Note.Eb));
    assertEquals("Gb7", Utils.chordName(BuiltinChordType.Dominant7, Note.Gb));
  }
}
