package ankijazzde.jlab.scales.anki;

import static de.jlab.scales.anki.FretboardJamCardGenerator.CAGED_MODES;
import static de.jlab.scales.anki.FretboardJamCardGenerator.CAGED_SCALES;
import static de.jlab.scales.anki.FretboardJamCardGenerator.PENTATONIC_CHORDS;
import static de.jlab.scales.anki.FretboardJamCardGenerator.PENTATONIC_SCALES;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.Utils;
import de.jlab.scales.anki.Deck;
import de.jlab.scales.anki.FretboardJamCardGenerator;
import de.jlab.scales.anki.JamCardGenerator;
import de.jlab.scales.anki.JamDeck;
import de.jlab.scales.theory.Note;

public class JamDeckProd {
 
  @Test
  @Ignore
  public void writeLimitedGuitarDeck() {
    Deck<?> fullDeck = new JamDeck(new JamCardGenerator("Practice Chords (Guitar)", Note.C, true));
    Deck<?> reducedDeck = scaleDown(fullDeck, 810);
    ProdUtils.writeTo(reducedDeck, 0.2);
  }

  @Test
  @Ignore
  public void writeLimitedCaged4GuitarDeck() {
    Deck<?> fullDeck = new JamDeck(new FretboardJamCardGenerator(CAGED_MODES, Utils.randomLoopIteratorFactory()));
    Deck<?> reducedDeck = scaleDown(fullDeck, 275);
    ProdUtils.writeTo(reducedDeck, 0.2);
  }
  
  private Deck<?> scaleDown(Deck<?> deck, double  fullSize) {
    deck.sort(0.2);
    int numberOfCards = (int)(deck.getCards().size() * 250.0 / fullSize);
    return deck.subdeck(numberOfCards);
  }

  @Test
  public void testFretboardJam() {
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(PENTATONIC_SCALES, Utils.randomLoopIteratorFactory())), 0.1);
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(PENTATONIC_CHORDS, Utils.randomLoopIteratorFactory())), 0.1);
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(CAGED_SCALES, Utils.randomLoopIteratorFactory())), 0.1);
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(CAGED_MODES, Utils.randomLoopIteratorFactory())), 0.1);
  }

}
