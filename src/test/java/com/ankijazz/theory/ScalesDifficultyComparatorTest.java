package com.ankijazz.theory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.ankijazz.theory.Note;
import com.ankijazz.theory.ScaleInfo;
import com.ankijazz.theory.ScaleUniverse;
import com.ankijazz.theory.Scales;
import com.ankijazz.theory.ScalesDifficultyComparator;
public class ScalesDifficultyComparatorTest {

  @Test
  public void testBug() {
    ScalesDifficultyComparator comp = new ScalesDifficultyComparator();
    ScaleInfo major = ScaleUniverse.SCALES.findFirstOrElseThrow(Scales.CMajor.transpose(Note.Gb));
    ScaleInfo melodicMinor = ScaleUniverse.SCALES.findFirstOrElseThrow(Scales.CMelodicMinor);
    assertThat(comp.compare(melodicMinor, major)).isPositive();
  }

}
