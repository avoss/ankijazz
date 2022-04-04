package com.ankijazz.lily;

import static com.ankijazz.lily.LilyRhythm.Type.PIANO;
import static com.ankijazz.rhythm.Event.b1;
import static com.ankijazz.rhythm.Event.b2;
import static com.ankijazz.rhythm.Event.r2;
import static com.ankijazz.rhythm.Quarter.q;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.ankijazz.rhythm.Quarter;
import com.ankijazz.rhythm.RandomRhythm;

public class LilyRhythmTest {

  @Test
  public void test() {
    LilyRhythm lily = new LilyRhythm(rhythm(), 70, PIANO);
    assertThat(lily.toLily()).contains("r8 a8 ~ a16 a16 r8");
  }

  private RandomRhythm rhythm() {
    Quarter q1 = q(r2, b2).tie();
    Quarter q2 = q(b1, b1, r2);
    return new RandomRhythm(List.of(q1, q2));
  }

}
