package com.ankijazz.anki;

import static com.ankijazz.midi.song.Ensembles.latin;
import static com.ankijazz.midi.song.SongFactory.Feature.AllKeys;

import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.Utils;
import com.ankijazz.anki.AbstractJamCardGenerator;
import com.ankijazz.anki.JamDeck;
import com.ankijazz.midi.song.Ensemble;
import com.ankijazz.midi.song.SongFactory.Feature;
import com.ankijazz.theory.Note;

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
