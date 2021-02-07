package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;

public class FretboardDiagramDeckTest {

  @Test
  public void testPentatonicsLevel3() {
    CardGenerator<FretboardDiagramCard> generator = new PentatonicsLevel3Generator();
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
//    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PentatonicsLevel3VisualizeChords.txt");
//    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "PentatonicsLevel3VisualizeChords.json");
    TestUtils.writeTo(deck, 0.1);
  }
  
  @Test
  public void testPentatonicsLevel5() {
    CardGenerator<FretboardDiagramCard> generator = new PentatonicsLevel5Generator();
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.writeTo(deck, 0.1);
  }

  @Test
  public void testCagedLevel3() {
    CardGenerator<FretboardDiagramCard> generator = new CagedLevel3Generator();
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.writeTo(deck, 0.1);
  }
}
