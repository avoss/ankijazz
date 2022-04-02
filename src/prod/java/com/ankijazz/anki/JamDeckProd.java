package com.ankijazz.anki;

import static com.ankijazz.anki.FretboardJamCardGenerator.CAGED_MODES;
import static com.ankijazz.anki.FretboardJamCardGenerator.CAGED_SCALES;
import static com.ankijazz.anki.FretboardJamCardGenerator.PENTATONIC_CHORDS;
import static com.ankijazz.anki.FretboardJamCardGenerator.PENTATONIC_SCALES;

import org.junit.Ignore;
import org.junit.Test;

import com.ankijazz.Utils;
import com.ankijazz.anki.Deck;
import com.ankijazz.anki.FretboardJamCardGenerator;
import com.ankijazz.anki.JamCardGenerator;
import com.ankijazz.anki.JamDeck;
import com.ankijazz.theory.Note;

public class JamDeckProd {
 
  /**
   * the full deck exceeds 250 MB which is the limit for Anki Shared Decks
   */
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
  public void writeChordJamDecks() {
    ProdUtils.writeTo(new JamDeck(new JamCardGenerator("Practice Chords (Guitar)", Note.C, true)), 0.2);
    ProdUtils.writeTo(new JamDeck(new JamCardGenerator("Practice Chords (C-Instrument)", Note.C, false)), 0.2);
    ProdUtils.writeTo(new JamDeck(new JamCardGenerator("Practice Chords (Bb-Instrument)", Note.Bb, false)), 0.2);
    ProdUtils.writeTo(new JamDeck(new JamCardGenerator("Practice Chords (Eb-Instrument)", Note.Eb, false)), 0.2);
  }
  
  @Test
  public void testFretboardJam() {
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(PENTATONIC_SCALES, Utils.randomLoopIteratorFactory())), 0.1);
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(PENTATONIC_CHORDS, Utils.randomLoopIteratorFactory())), 0.1);
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(CAGED_SCALES, Utils.randomLoopIteratorFactory())), 0.1);
    ProdUtils.writeTo(new JamDeck(new FretboardJamCardGenerator(CAGED_MODES, Utils.randomLoopIteratorFactory())), 0.1);
  }

}
