package de.jlab.scales.anki;

import static de.jlab.scales.anki.FretboardJamCardGenerator.CAGED_MODES;
import static de.jlab.scales.anki.FretboardJamCardGenerator.CAGED_SCALES;
import static de.jlab.scales.anki.FretboardJamCardGenerator.PENTATONIC_CHORDS;
import static de.jlab.scales.anki.FretboardJamCardGenerator.PENTATONIC_SCALES;
import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;

import java.util.Set;
import java.util.function.Supplier;

import org.junit.Ignore;
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
  @Ignore
  public void writeAllInstrumentsPreviewOnly() {
    TestUtils.writePreviewOnly(new JamDeck(new JamCardGenerator("Practice Chords (Guitar)", Note.C, true)), 0.2);
    TestUtils.writePreviewOnly(new JamDeck(new JamCardGenerator("Practice Chords (C-Instrument)", Note.C, false)), 0.2);
    TestUtils.writePreviewOnly(new JamDeck(new JamCardGenerator("Practice Chords (Bb-Instrument)", Note.Bb, false)), 0.2);
    TestUtils.writePreviewOnly(new JamDeck(new JamCardGenerator("Practice Chords (Eb-Instrument)", Note.Eb, false)), 0.2);
  }
  
  @Test
  public void writeTestDeck() {
    JamDeck deck = new JamDeck(new TestCardGenerator());
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "JamDeckTest.txt");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "JamDeckTest.json");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "JamDeckTest.html");
  }

}
