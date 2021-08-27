package ankijazzde.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.anki.ModesPracticeDeck;
import de.jlab.scales.anki.ModesPracticeGuitarDeck;
import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Note;

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
