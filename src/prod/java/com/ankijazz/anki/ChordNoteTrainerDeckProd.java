package com.ankijazz.anki;

import org.junit.Test;

public class ChordNoteTrainerDeckProd {

  @Test
  public void writeDeck() {
    ProdUtils.writeTo(new ChordNoteTrainerDeck(), 0.2);
  }
  
}
