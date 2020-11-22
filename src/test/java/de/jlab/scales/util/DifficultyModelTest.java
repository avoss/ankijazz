package de.jlab.scales.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.util.DifficultyModel.BooleanFactor;
import de.jlab.scales.util.DifficultyModel.DoubleFactor;

public class DifficultyModelTest {

  @Test
  public void testDoubleFactor() {
    DifficultyModel model = new DifficultyModel(100);
    DoubleFactor factor = DoubleFactor.builder().model(model).min(20).max(40).weight(10).build();
    assertEquals(0, factor.update(10));
    assertEquals(0, factor.update(20));
    assertEquals(50, factor.update(30));
    assertEquals(99, factor.update(39.999));
    assertEquals(100, factor.update(40));
    assertEquals(100, factor.update(99));
  }

  @Test
  public void testBooleanFactor() {
    DifficultyModel model = new DifficultyModel(100);
    BooleanFactor factor = BooleanFactor.builder().model(model).difficultValue(false).weight(10).build();
    assertEquals(0, factor.update(true));
    assertEquals(100, factor.update(false));
  }

  @Test
  public void testCombined() {
    DifficultyModel model = new DifficultyModel(100);
    BooleanFactor booleanFactor = BooleanFactor.builder().model(model).difficultValue(false).weight(10).build();
    DoubleFactor doubleFactor = DoubleFactor.builder().model(model).min(20).max(40).weight(20).build();
    assertEquals(33, booleanFactor.update(false));
    assertEquals(66, doubleFactor.update(30));
  }
}
