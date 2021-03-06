package com.ankijazz.rhythm;

import static com.ankijazz.rhythm.Event.b1;
import static com.ankijazz.rhythm.Event.b2;
import static com.ankijazz.rhythm.Event.b3;
import static com.ankijazz.rhythm.Event.bt;
import static com.ankijazz.rhythm.Event.r2;
import static com.ankijazz.rhythm.Event.rt;
import static com.ankijazz.rhythm.Quarter.q;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.ankijazz.rhythm.RhythmTransformer.Tick;

public class RhythmTransformerTest {

  @Test
  public void testTicksPerQuarter() {
    RhythmTransformer t = new RhythmTransformer();
    assertEquals(1, t.ticksPerQuarter(List.of(q(b1))));
    assertEquals(3, t.ticksPerQuarter(List.of(q(b1, r2))));
    assertThatThrownBy(() -> t.ticksPerQuarter(List.of(q(b1, r2), q(b1)))).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> t.ticksPerQuarter(List.of(q(bt, bt, rt)))).isInstanceOf(IllegalArgumentException.class);
  }
  
  @Test
  public void noExtraBeatsShouldBeCreated() {
    assertTransform(2, "r2b1b1", b1, b3);
    assertTransform(1, "r3b1", r2, b2);
    assertTransform(1, "r1b2b1", b2, b2);
  }
  
  private void assertTransform(int distance, String expected, Event ... events) {
    RhythmTransformer t = new RhythmTransformer();
    List<Quarter> actual = t.transpose(List.of(q(events)), distance);
    assertEquals(expected, actual.get(0).toString());
  }

  @Test
  public void testDisassembleAssemble() {
    RhythmTransformer transformer = new RhythmTransformer();
    List<Quarter> origin = origin();
    assertFalse(origin.get(origin.size()-1).isTied());
    List<Tick> ticks = transformer.disassemble(origin);
    assertFalse(ticks.get(ticks.size()-1).isTied());
    assertEquals("bb bbb bb bbb bb b rr b ", toString(ticks));
    List<Quarter> assembled = transformer.assemble(ticks, 4);
    assertEquals(origin.toString(), assembled.toString());
    Collections.rotate(ticks, 1);
    assertEquals("b bb bbb bb bbb bb b rr ", toString(ticks));
    assertEquals("[b1b2b1 ~, b2b2, b3b1 ~, b1b1r2]", transformer.assemble(ticks, 4).toString());
  }

  private String toString(List<Tick> ticks) {
    StringBuilder sb = new StringBuilder();
    for (Tick tick : ticks) {
      sb.append(tick.isBeat() ? "b" : "r");
      if (!tick.isTied()) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  @Test
  public void testTranspose() {
    RhythmTransformer transformer = new RhythmTransformer();
    List<Quarter> origin = origin();
    List<Quarter> actual = transformer.transpose(origin, 1);
    List<Quarter> transposed = transposed();
    assertEquals(transposed.toString(), actual.toString());
    assertEquals(origin, transformer.transpose(transposed, -1));
    
  }

  private List<Quarter> origin() {
    // x~x~ ~x~x ~~x~ x..x
    Quarter q1 = q(b2, b2).tie();
    Quarter q2 = q(b1, b2, b1).tie();
    Quarter q3 = q(b2, b2);
    Quarter q4 = q(b1,r2,b1);
    return List.of(q1, q2, q3, q4);
  }
  
  private List<Quarter> transposed() {
    // xx~x ~~x~ x~~x ~x..
    Quarter q1 = q(b1, b2, b1).tie();
    Quarter q2 = q(b2, b2);
    Quarter q3 = q(b3, b1).tie();
    Quarter q4 = q(b1, b1, r2);
    return List.of(q1, q2, q3, q4);
    
  }

}
