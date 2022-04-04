package com.ankijazz.rhythm;

import static com.ankijazz.rhythm.Event.b1;
import static com.ankijazz.rhythm.Event.b2;
import static com.ankijazz.rhythm.Event.b4;
import static com.ankijazz.rhythm.Event.bt;
import static com.ankijazz.rhythm.Event.r1;
import static com.ankijazz.rhythm.Event.r3;
import static com.ankijazz.rhythm.Event.rt;
import static com.ankijazz.rhythm.Quarter.q;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

public class QuarterTest {

  @Test
  public void testLength() {
    Quarter q = new Quarter();
    q = q.add(b1);
    assertEquals(Fraction.ONE, q.getLength());
    q = q.add(r3);
    assertEquals(new Fraction(4), q.getLength());
    q = q.add(rt);
    assertEquals(new Fraction(16, 3), q.getLength());
  }

  @Test
  public void testRestsAreCombined() {
    Quarter q = new Quarter();
    assertEquals(0, q.getNumberOfEvents());
    q = q.add(r1);
    assertEquals(1, q.getNumberOfEvents());
    q = q.add(r1);
    assertEquals(Fraction.TWO, q.getLength());
    assertEquals(1, q.getNumberOfEvents());
    q = q.add(b1);
    assertEquals(new Fraction(3), q.getLength());
    assertEquals(2, q.getNumberOfEvents());
    q = q.add(r3);
    assertEquals(new Fraction(6), q.getLength());
    assertEquals(3, q.getNumberOfEvents());
    q = q.add(r1);
    assertEquals(new Fraction(7), q.getLength());
    assertEquals(3, q.getNumberOfEvents());
  }

  @Test
  public void testDifficulty() {
    Fraction f = new Fraction(16, 4);
    f = f.divide(4);
    assertEquals(1, f.getDenominator());
    assertThat(q(b4).getDifficulty()).isLessThan(q(b2, b2).getDifficulty());
    assertThat(q(b1, b2, b1).getDifficulty()).isGreaterThan(q(b2, b2).getDifficulty());
    assertThat(q(r1, b2, b1).getDifficulty()).isGreaterThan(q(b1, b2, b1).getDifficulty());
    assertThat(q(b2, b2).tie().getDifficulty()).isGreaterThan((q(b2, b2).getDifficulty()));
    assertThat(q(rt, bt, bt).getDifficulty()).isGreaterThan((q(bt, bt, bt).getDifficulty()));
  }

  @Test
  public void testStartsEndsWithBeat() {
    Quarter q = new Quarter();
    assertFalse(q.startsWithBeat());
    assertFalse(q.endsWithBeat());

    q = new Quarter(b1);
    assertTrue(q.startsWithBeat());
    assertTrue(q.endsWithBeat());

    q = new Quarter(r1);
    assertFalse(q.startsWithBeat());
    assertFalse(q.endsWithBeat());

    q = new Quarter(b1, r1);
    assertTrue(q.startsWithBeat());
    assertFalse(q.endsWithBeat());

    q = new Quarter(r1, b1);
    assertFalse(q.startsWithBeat());
    assertTrue(q.endsWithBeat());
  }

  @Test
  public void testHashEquals() {
    Quarter q1 = new Quarter();
    Quarter q2 = new Quarter();
    assertEquals(q1, q2);
    q1 = q1.add(r1);
    assertNotEquals(q1, q2);
    q2 = q2.add(r1);
    assertEquals(q1, q2);
    assertNotSame(q1, q2);
  }
}
