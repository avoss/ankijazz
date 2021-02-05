package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;

public class FretboardDiagramDeckTest {

  @Test
  public void test() {
    CardGenerator<FretboardDiagramCard> generator = new OutlineChordsWithPentatonicsCardGenerator(Utils.fixedLoopIteratorFactory());
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.writeTo(deck, 0);
  }

}
