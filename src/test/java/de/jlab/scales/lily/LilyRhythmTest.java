package de.jlab.scales.lily;

import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.jlab.scales.rhythm.EventSequence;
import de.jlab.scales.rhythm.Rhythm;

public class LilyRhythmTest {

  @Test
  public void test() {
    EventSequence s1 = EventSequence.of(r2, b2);
    EventSequence s2 = EventSequence.of(b1, b1, r2);
    Rhythm rhythm = Rhythm.of(Arrays.asList(s1, s2), Collections.singleton(s1));
    LilyRhythm lily = new LilyRhythm(rhythm);
    assertThat(lily.toLily()).contains("r1 r8 a8 ~ a16 a16 r8");
  }

}
