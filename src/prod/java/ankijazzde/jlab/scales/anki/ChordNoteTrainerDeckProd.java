package ankijazzde.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.anki.ChordNoteTrainerDeck;

public class ChordNoteTrainerDeckProd {

  @Test
  public void writeDeck() {
    ProdUtils.writeTo(new ChordNoteTrainerDeck(), 0.2);
  }
  
}
