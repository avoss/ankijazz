package de.jlab.scales.lily;

import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.rhythm.EventSequence;
import de.jlab.scales.rhythm.Rhythm;

public class LilyRhythmTest {

  @Test
  public void test() {
    LilyRhythm lily = new LilyRhythm(rhythm());
    assertThat(lily.toLily()).contains("r8 a8 ~ a16 a16 r8");
  }

  private Rhythm rhythm() {
    EventSequence s1 = EventSequence.of(r2, b2);
    EventSequence s2 = EventSequence.of(b1, b1, r2);
    return new Rhythm(List.of(s1, s2), singleton(s1));
  }

}
