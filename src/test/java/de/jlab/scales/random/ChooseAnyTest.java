package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.jlab.scales.theory.Accidental;

public class ChooseAnyTest {

  @Test
  public void testMultipleValues() {
    ChooseAny<Accidental> generator = new ChooseAny<Accidental>(Accidental.FLAT, Accidental.SHARP);
    Accidental actual = generator.next();
    assertTrue(actual == Accidental.FLAT || actual == Accidental.SHARP);
  }
  
  @Test
  public void testSingleValue() {
    final String STRING = "Hello";
    ChooseAny<String> generator = new ChooseAny<String>(STRING);
    for (int i = 0; i < 100; i++) {
      assertEquals(STRING, generator.next());
    }
  }

}
