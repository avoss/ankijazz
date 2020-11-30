package de.jlab.scales.difficulty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.assertj.core.data.Offset;
import org.junit.Test;

import de.jlab.scales.difficulty.DifficultyModel.BooleanFactor;
import de.jlab.scales.difficulty.DifficultyModel.DoubleFactor;

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
    DoubleFactor factor = new DoubleFactor(20, 40, value);
    assertThat(factor.update(value).getDifficulty()).isCloseTo(expected, EPS);
  }

  @Test
  public void testDoubleFactorEquals() {
    DoubleFactor f1 = new DoubleFactor(20, 40, 20);
    DoubleFactor f2 = new DoubleFactor(20, 40, 20);
    assertEquals(f1, f2);
    f1.update(10);
    f2.update(20);
    assertEquals(f1, f2);
    
    DoubleFactor f3 = new DoubleFactor(10, 40, 20);
    assertNotEquals(f1, f3);
  }
  
  @Test
  public void testBooleanFactorDifficulty() {
    BooleanFactor factor = new BooleanFactor(10);
    assertThat(factor.update(true).getDifficulty()).isCloseTo(1, EPS);
    assertThat(factor.update(false).getDifficulty()).isCloseTo(0, EPS);
  }

  @Test
  public void testBooleanFactorEquals() {
    BooleanFactor f1 = new BooleanFactor(10);
    BooleanFactor f2 = new BooleanFactor(10);
    assertEquals(f1, f2);
    f1.update(true);
    f2.update(false);
    assertEquals(f1, f2);
    BooleanFactor f3 = new BooleanFactor(20);
    assertNotEquals(f2, f3);
  }
  
  @Test
  public void testCombined() {
    DifficultyModel model = new DifficultyModel();
    BooleanFactor booleanFactor = model.booleanFactor(10);
    DoubleFactor doubleFactor = model.doubleFactor(20, 40, 20);
    booleanFactor.update(true);
    assertThat(model.getDifficulty()).isCloseTo(0.333, EPS);
    doubleFactor.update(30);
    assertThat(model.getDifficulty()).isCloseTo(0.666, EPS);
  }
}
