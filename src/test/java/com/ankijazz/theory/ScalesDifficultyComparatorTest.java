package de.jlab.scales.theory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
public class ScalesDifficultyComparatorTest {

  @Test
  public void testBug() {
    ScalesDifficultyComparator comp = new ScalesDifficultyComparator();
    ScaleInfo major = ScaleUniverse.SCALES.findFirstOrElseThrow(Scales.CMajor.transpose(Note.Gb));
    ScaleInfo melodicMinor = ScaleUniverse.SCALES.findFirstOrElseThrow(Scales.CMelodicMinor);
    assertThat(comp.compare(melodicMinor, major)).isPositive();
  }

}
