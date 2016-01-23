package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FixedSequenceTest {

  @Test
  public void testWitLocalContext() {
    Sequence<Integer> values = new FixedSequence<Integer>(1,2,3,4);
    TestUtil.assertInteger(values, 1, 2, 3, 4, 1, 2, 3, 4, 1);
  }

  @Test
  public void assertThatLiveValuesAreReturned() {
    Integer[] values = new Integer[]{1};
    Sequence<Integer> sequence = new FixedSequence<Integer>(values);
    assertEquals(1, sequence.next().intValue());
    values[0] = 2;
    assertEquals(2, sequence.next().intValue());
  }
  
  @Test
  public void testWitGlobalContext() {
    ContextImpl context = new ContextImpl();
    Sequence<Integer> values = new FixedSequence<Integer>(context, 1,2,3,4);
    TestUtil.assertInteger(values, 1, 1, 1);
    context.next();
    TestUtil.assertInteger(values, 2, 2, 2);
  }
  
//  @Test
//  public void testWithPattern() {
//    Sequence<Integer> sequence = Sequences.fixedSequence("0123456789x", 0, 100); // new FixedSequence<Integer>("0123456789x", 0, 100);
//    TestUtil.assertInteger(sequence, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
//  }
}
