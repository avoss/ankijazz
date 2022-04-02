package ankijazzde.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.anki.PentaChordsTheoryDeck;

public class PentaChordsTheoryDeckProd {

  @Test
  public void writePentaChordsTheoryDeck() {
    ProdUtils.writeTo(new PentaChordsTheoryDeck(), 0.3);
  }

}
