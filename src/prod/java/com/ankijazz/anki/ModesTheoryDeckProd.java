package com.ankijazz.anki;

import org.junit.Test;

public class ModesTheoryDeckProd {

  @Test
  public void writeModesTheoryDeck() {
    ProdUtils.writeTo((Deck<?>) new ModesTheoryDeck(true), 0.10);
  }

  @Test
  public void writeScalesTheoryDeck() {
    ProdUtils.writeTo((Deck<?>) new ModesTheoryDeck(false), 0.10);
  }
  
}
