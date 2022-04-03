package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.lily.Clef;
import com.ankijazz.theory.Note;

public class ModesPracticeDeckProd {

  private static final double RND = 0.2;

  @Test
  public void writePlayModesDeckC() {
    ProdUtils.writeTo(new ModesPracticeDeck(Note.C), RND);
  }

  @Test
  public void writePlayModesDeckBass() {
    ProdUtils.writeTo(new ModesPracticeDeck(Clef.BASS), RND);
  }

  @Test
  public void writePlayModesDeckBb() {
    ProdUtils.writeTo(new ModesPracticeDeck(Note.Bb), RND);
  }

  @Test
  public void writePlayModesDeckEb() {
    ProdUtils.writeTo(new ModesPracticeDeck(Note.Eb), RND);
  }
  
  @Test
  public void writePlayModesGuitarDeck() {
    ProdUtils.writeTo(new ModesPracticeGuitarDeck(), RND);
  }
}
