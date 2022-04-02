package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.anki.PentaChordsTheoryDeck;

public class PentaChordsTheoryDeckProd {

  @Test
  public void writePentaChordsTheoryDeck() {
    ProdUtils.writeTo(new PentaChordsTheoryDeck(), 0.3);
  }

}
