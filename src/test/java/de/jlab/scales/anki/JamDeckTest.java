package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;

import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.midi.song.SongFactory.Feature;
import de.jlab.scales.theory.Note;

public class JamDeckTest {
 
  class TestCardGenerator extends AbstractJamCardGenerator {

    protected TestCardGenerator() {
      super("Test Jam Deck", Note.C, true, Utils.fixedLoopIteratorFactory());
    }

    @Override
    protected void addCards() {
      Set<Supplier<Ensemble>> ensembles =  Set.of(() -> latin(125));
      addCards(Set.of(Feature.Test, AllKeys), ensembles);
    }
    
  }
  
  @Test
  public void writeTestDeck() {
    JamDeck deck = new JamDeck(new TestCardGenerator());
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "JamDeckTest.txt");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "JamDeckTest.json");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "JamDeckTest.html");
  }

}
