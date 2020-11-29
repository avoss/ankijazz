package de.jlab.scales.difficulty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.assertj.core.data.Offset;
import org.junit.Test;

import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.difficulty.DifficultyModel.BooleanFactor;
import de.jlab.scales.difficulty.DifficultyModel.DoubleFactor;

import static org.assertj.core.api.Assertions.*;

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
    DoubleFactor factor = DoubleFactor.builder().min(20).max(40).weight(value).build();
    assertThat(factor.update(value).getDifficulty()).isCloseTo(expected, EPS);
  }

  @Test
  public void testDoubleFactorEquals() {
    DoubleFactor f1 = DoubleFactor.builder().min(20).max(40).weight(20).build();
    DoubleFactor f2 = DoubleFactor.builder().min(20).max(40).weight(20).build();
    assertEquals(f1, f2);
    f1.update(10);
    f2.update(20);
    assertEquals(f1, f2);
    
    DoubleFactor f3 = DoubleFactor.builder().min(10).max(40).weight(20).build();
    assertNotEquals(f1, f3);
  }
  
  @Test
  public void testBooleanFactorDifficulty() {
    BooleanFactor factor = BooleanFactor.builder().difficultValue(false).weight(10).build();
    assertThat(factor.update(true).getDifficulty()).isCloseTo(0, EPS);
    assertThat(factor.update(false).getDifficulty()).isCloseTo(1, EPS);
  }

  @Test
  public void testBooleanFactorEquals() {
    BooleanFactor f1 = BooleanFactor.builder().difficultValue(true).build();
    BooleanFactor f2 = BooleanFactor.builder().difficultValue(true).build();
    assertEquals(f1, f2);
    f1.update(true);
    f2.update(false);
    assertEquals(f1, f2);
    BooleanFactor f3 = BooleanFactor.builder().difficultValue(false).build();
    assertNotEquals(f2, f3);
  }
  
  @Test
  public void testCombined() {
    DifficultyModel model = new DifficultyModel();
    BooleanFactor booleanFactor = BooleanFactor.builder().difficultValue(false).weight(10).build();
    DoubleFactor doubleFactor = DoubleFactor.builder().min(20).max(40).weight(20).build();
    model.register(doubleFactor);
    model.register(booleanFactor);
    booleanFactor.update(false);
    assertThat(model.getDifficulty()).isCloseTo(0.333, EPS);
    doubleFactor.update(30);
    assertThat(model.getDifficulty()).isCloseTo(0.666, EPS);
  }
}
