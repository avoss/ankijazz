package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleCardTest {

  @Test
  public void test() {
    SimpleCard card = new SimpleCard(0.1, "front", "back");
    card.put("back", "b");
    card.put("front", "a");
    card.put("front", "x");
    assertEquals("x\tb", card.getCsv());
    
  }

}
