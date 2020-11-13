package de.jlab.scales.rhythm;

import static de.jlab.scales.rhythm.Event.*;
import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

import de.jlab.scales.rhythm.EventSequence;

public class EventSequenceTest {

  @Test
  public void testLength() {
    EventSequence seq = new EventSequence();
    seq = seq.add(b1);
    assertEquals(Fraction.ONE, seq.getLength());
    seq = seq.add(r3);
    assertEquals(new Fraction(4), seq.getLength());
//    seq = seq.add(rt);
//    assertEquals(new Fraction(16, 3), seq.getLength());
  }

  @Test
  public void testRestsAreCombined() {
    EventSequence seq = new EventSequence();
    assertEquals(0, seq.getNumberOfEvents());
    seq = seq.add(r1);
    assertEquals(1, seq.getNumberOfEvents());
    seq = seq.add(r1);
    assertEquals(Fraction.TWO, seq.getLength());
    assertEquals(1, seq.getNumberOfEvents());
    seq = seq.add(b1);
    assertEquals(new Fraction(3), seq.getLength());
    assertEquals(2, seq.getNumberOfEvents());
    seq = seq.add(r3);
    assertEquals(new Fraction(6), seq.getLength());
    assertEquals(3, seq.getNumberOfEvents());
    seq = seq.add(r1);
    assertEquals(new Fraction(7), seq.getLength());
    assertEquals(3, seq.getNumberOfEvents());
  }
  
  @Test 
  public void testDifficulty() {
    EventSequence s = new EventSequence(r1,b4);
    assertEquals(r1.getDifficulty() + b4.getDifficulty(), s.getDifficulty());
  }
  
  @Test
  public void testStartsEndsWithBeat() {
    EventSequence s = new EventSequence();
    assertFalse(s.startsWithBeat());
    assertFalse(s.endsWithBeat());

    s = new EventSequence(b1);
    assertTrue(s.startsWithBeat());
    assertTrue(s.endsWithBeat());

    s = new EventSequence(r1);
    assertFalse(s.startsWithBeat());
    assertFalse(s.endsWithBeat());
    
    s = new EventSequence(b1,r1);
    assertTrue(s.startsWithBeat());
    assertFalse(s.endsWithBeat());
    
    s = new EventSequence(r1,b1);
    assertFalse(s.startsWithBeat());
    assertTrue(s.endsWithBeat());
  }
  
  @Test
  public void testEventSequenceCategory() {
    EventSequence s1 = new EventSequence(r1,b1);
    EventSequence s2 = new EventSequence(r1,b2,r3);
    assertEquals(s1.getCategory(), s2.getCategory());
    s2 = s2.add(b1);
    assertNotEquals(s1.getCategory(), s2.getCategory());
  }
  
  @Test
  public void testHashEquals() {
    EventSequence s1 = new EventSequence();
    EventSequence s2 = new EventSequence();
    assertEquals(s1, s2);
    s1 = s1.add(r1);
    assertNotEquals(s1, s2);
    s2 = s2.add(r1);
    assertEquals(s1, s2);
    assertNotSame(s1, s2);
  }
}
