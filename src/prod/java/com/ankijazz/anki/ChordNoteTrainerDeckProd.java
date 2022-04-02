package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.anki.ChordNoteTrainerDeck;

public class ChordNoteTrainerDeckProd {

  @Test
  public void writeDeck() {
    ProdUtils.writeTo(new ChordNoteTrainerDeck(), 0.2);
  }
  
}
