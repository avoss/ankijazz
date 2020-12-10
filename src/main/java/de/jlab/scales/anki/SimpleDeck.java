package de.jlab.scales.anki;

import java.util.List;

public class SimpleDeck extends AbstractDeck {

  public SimpleDeck(String title) {
    super(title);
  }

  public SimpleDeck(String title, String outputFileName, String mustacheTemplate, List<Card> cards) {
    super(title, outputFileName, mustacheTemplate, cards);
  }

}
