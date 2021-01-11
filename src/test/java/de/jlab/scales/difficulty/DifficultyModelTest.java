package de.jlab.scales.difficulty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.assertj.core.data.Offset;
import org.junit.Test;

import de.jlab.scales.difficulty.DifficultyModel.BooleanTerm;
import de.jlab.scales.difficulty.DifficultyModel.DoubleTerm;

public class DifficultyModelTest {

  private static final Offset<Double> EPS = Offset.offset(0.01);

  @Test
  public void testDoubleFactorDifficulty() {
    assertDoubleFactor(10, 0.0);
    assertDoubleFactor(20, 0.0);
    assertDoubleFactor(30, 0.5);
    assertDoubleFactor(39, 0.95);
    assertDoubleFactor(40, 1.0);
    assertDoubleFactor(99, 1.0);
  }

  private void assertDoubleFactor(int value, double expected) {
    DoubleTerm term = new DoubleTerm(20, 40, value);
    assertThat(term.update(value).getDifficulty()).isCloseTo(expected, EPS);
  }

  @Test
  public void testDoubleFactorEquals() {
    DoubleTerm t1 = new DoubleTerm(20, 40, 20);
    DoubleTerm t2 = new DoubleTerm(20, 40, 20);
    assertEquals(t1, t2);
    t1.update(10);
    t2.update(20);
    assertEquals(t1, t2);
    
    DoubleTerm t3 = new DoubleTerm(10, 40, 20);
    assertNotEquals(t1, t3);
  }
  
  @Test
  public void testBooleanFactorDifficulty() {
    BooleanTerm term = new BooleanTerm(10);
    assertThat(term.update(true).getDifficulty()).isCloseTo(1, EPS);
    assertThat(term.update(false).getDifficulty()).isCloseTo(0, EPS);
  }

  @Test
  public void testBooleanFactorEquals() {
    BooleanTerm t1 = new BooleanTerm(10);
    BooleanTerm t2 = new BooleanTerm(10);
    assertEquals(t1, t2);
    t1.update(true);
    t2.update(false);
    assertEquals(t1, t2);
    BooleanTerm t3 = new BooleanTerm(20);
    assertNotEquals(t2, t3);
  }
  
  @Test
  public void testCombined() {
    DifficultyModel model = new DifficultyModel();
    BooleanTerm booleanTerm = model.booleanTerm(10);
    DoubleTerm doubleTerm = model.doubleTerm(20, 40, 20);
    booleanTerm.update(true);
    assertThat(model.getDifficulty()).isCloseTo(0.333, EPS);
    doubleTerm.update(30);
    assertThat(model.getDifficulty()).isCloseTo(0.666, EPS);
  }
  
  @Test
  public void testBugFix() {
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(100).update(0.5);
    model.doubleTerm(100).update(1.0);
    assertThat(model.getDifficulty()).isCloseTo(0.75, EPS);
    
  }
}
