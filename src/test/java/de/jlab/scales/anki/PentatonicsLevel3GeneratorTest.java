package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.anki.PentatonicsLevel3VisualizeChords.ChordPentaPair;
import de.jlab.scales.theory.Note;

public class PentatonicsLevel3VisualizeChordsTest {

  @Test
  public void testGenerator() {
    PentatonicsLevel3VisualizeChords generator = new PentatonicsLevel3VisualizeChords(Utils.fixedLoopIteratorFactory());
    assertThat(generator.findPairs()).contains(new ChordPentaPair(Major7, Minor7Pentatonic.getPrototype().transpose(Note.E)));
  }
  
  @Test
  public void testWriteDeck() {
    CardGenerator<FretboardDiagramCard> generator = new PentatonicsLevel3VisualizeChords(Utils.fixedLoopIteratorFactory());
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "PentatonicsLevel3VisualizeChords.json");
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PentatonicsLevel3VisualizeChords.txt");
    TestUtils.writeTo(deck, 0.1);
  }
  

}
