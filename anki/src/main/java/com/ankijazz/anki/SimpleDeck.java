package com.ankijazz.anki;

import java.util.List;

public class SimpleDeck<T extends Card> extends AbstractDeck<T> {

  public SimpleDeck(String title) {
    super(title);
  }

  public SimpleDeck(String title, String outputFileName, String mustacheTemplate, List<T> cards) {
    super(title, outputFileName, mustacheTemplate, cards);
  }

  public SimpleDeck(String title, String outputFileName) {
    super(title, outputFileName);
  }

}
