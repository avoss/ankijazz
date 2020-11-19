package de.jlab.scales.rhythm;

import static de.jlab.scales.rhythm.Event.*;
import static de.jlab.scales.rhythm.Quarter.q;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.rhythm.RhythmTransformer.Tick;

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

  @Test
  public void testMultipleDistances() {
    RhythmTransformer transformer = new RhythmTransformer();
    List<Quarter> origin = origin();
    for (int i = 0; i < origin.size(); i++) {
      List<Quarter> transposed = transformer.transpose(origin, i);
      transposed = transformer.transpose(transposed,  -i);
      assertFalse(origin.get(transposed.size()-1).isTied());
      assertEquals(origin, transposed);
     }
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
