package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Note;

public class ModesPracticeDeckProd {

  private static final double RND = 0.2;

  @Test
  public void testPlayModesDeckC() {
    TestUtils.writeTo(new ModesPracticeDeck(Note.C), RND);
  }

  @Test
  public void testPlayModesDeckBass() {
    TestUtils.writeTo(new ModesPracticeDeck(Clef.BASS), RND);
  }

  @Test
  public void testPlayModesDeckBb() {
    TestUtils.writeTo(new ModesPracticeDeck(Note.Bb), RND);
  }

  @Test
  public void testPlayModesDeckEb() {
    TestUtils.writeTo(new ModesPracticeDeck(Note.Eb), RND);
  }
  
  @Test
  public void testPlayModesGuitarDeck() {
    TestUtils.writeTo(new ModesPracticeGuitarDeck(), RND);
  }
}
