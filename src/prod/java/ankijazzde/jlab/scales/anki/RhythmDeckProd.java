package ankijazzde.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.anki.Deck;
import de.jlab.scales.anki.RhythmDeck;
import de.jlab.scales.lily.LilyRhythm.Type;

public class RhythmDeckProd {

  @Test
  public void generateAll() {
    ProdUtils.writeTo((Deck<?>) new RhythmDeck(Type.PIANO, 60, 81), 0);
    ProdUtils.writeTo((Deck<?>) new RhythmDeck(Type.PIANO, 100, 121), 0);
    ProdUtils.writeTo((Deck<?>) new RhythmDeck(Type.PIANO, 120, 121), 0);
  }
  
}
