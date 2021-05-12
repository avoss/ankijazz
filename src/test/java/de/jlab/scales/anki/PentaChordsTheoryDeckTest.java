package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

public class PentaChordsTheoryDeckTest {

  @Test
  public void test() {
    TestUtils.writeTo(new PentaChordsTheoryDeck(), 0.3);
  }

}
