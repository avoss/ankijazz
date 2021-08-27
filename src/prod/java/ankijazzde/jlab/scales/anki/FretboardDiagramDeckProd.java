package de.jlab.scales.anki;

import java.util.function.Supplier;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.ScaleInfo;

public class FretboardDiagramDeckProd {

  class Validator implements AbstractFretboardGenerator.Validator {
    @Override
    public void validate(Fretboard frontBoard, Fretboard backBoard) {
      
    }
    @Override
    public void validate(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
      
    }
    @Override
    public void validate(Supplier<Part> backMidi) {
      
    }
  }
  
  Validator validator = new Validator();

  @Test
  public void testPentatonic1ScalesFretboards() {
    writeDeck(new Pentatonic1ScalesFretboards(validator));
  }

  private void writeDeck(CardGenerator<FretboardDiagramCard> generator) {
    TestUtils.writeTo(new FretboardDiagramDeck(generator), 0.1);
  }
  
  @Test
  public void testPentatonic3ChordsFretboards() {
    writeDeck(new Pentatonic3ChordsFretboards(validator));
  }
  
  @Test
  public void testPentatonic5ModesFretboards() {
    writeDeck(new Pentatonic5ModesFretboards(validator));
  }

  @Test
  public void testCaged1ScalesFretboards() {
    writeDeck(new Caged1ScalesFretboards(validator));
  }

  @Test
  public void testCaged3ModesFretboards() {
    writeDeck(new Caged3ModesFretboards(validator));
  }
  
  @Test
  public void testCaged5ArpeggiosFretboards() {
    writeDeck(new Caged5ArpeggiosFretboards(validator));
  }
  
}
