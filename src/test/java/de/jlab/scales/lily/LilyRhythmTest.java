package de.jlab.scales.lily;

import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static de.jlab.scales.rhythm.Quarter.q;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.rhythm.Quarter;
import de.jlab.scales.rhythm.RandomRhythm;

public class LilyRhythmTest {

  @Test
  public void test() {
    LilyRhythm lily = new LilyRhythm(rhythm(), 70);
    assertThat(lily.toLily()).contains("r8 a8 ~ a16 a16 r8");
  }

  private RandomRhythm rhythm() {
    Quarter q1 = q(r2, b2).tie();
    Quarter q2 = q(b1, b1, r2);
    return new RandomRhythm(List.of(q1, q2));
  }

}
