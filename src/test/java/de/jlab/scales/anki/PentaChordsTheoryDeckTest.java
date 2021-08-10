package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class PentaChordsTheoryDeckTest {

  @Test
  public void test() {
    TestUtils.writeTo(new PentaChordsTheoryDeck(), 0.3);
  }

}
