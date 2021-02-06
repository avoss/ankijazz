package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.anki.AbstractFretboardGenerator.ScaleChordPair;
import de.jlab.scales.theory.Note;

public class PentatonicsLevel3GeneratorTest {

  @Test
  public void testGenerator() {
    PentatonicsLevel3Generator generator = new PentatonicsLevel3Generator(Utils.fixedLoopIteratorFactory());
    assertThat(generator.findPairs().stream()).contains(new ScaleChordPair(Major7.getPrototype(), Minor7Pentatonic.getPrototype().transpose(Note.E)));
  }
  
  @Test
  public void testWriteDeck() {
    CardGenerator<FretboardDiagramCard> generator = new PentatonicsLevel3Generator(Utils.fixedLoopIteratorFactory());
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PentatonicsLevel3VisualizeChords.txt");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "PentatonicsLevel3VisualizeChords.json");
    TestUtils.writeTo(deck, 0.1);
  }
  

}
