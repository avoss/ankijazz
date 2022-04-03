package com.ankijazz.anki;

import java.util.function.Supplier;

import org.junit.Test;

import com.ankijazz.fretboard.Fretboard;
import com.ankijazz.midi.Part;
import com.ankijazz.theory.ScaleInfo;

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
  public void writePentatonic1ScalesFretboards() {
    writeDeck(new Pentatonic1ScalesFretboards(validator));
  }

  @Test
  public void writePentatonic3ChordsFretboards() {
    writeDeck(new Pentatonic3ChordsFretboards(validator));
  }
  
  @Test
  public void writePentatonic5ModesFretboards() {
    writeDeck(new Pentatonic5ModesFretboards(validator));
  }

  @Test
  public void writeCaged1ScalesFretboards() {
    writeDeck(new Caged1ScalesFretboards(validator));
  }

  @Test
  public void writeCaged3ModesFretboards() {
    writeDeck(new Caged3ModesFretboards(validator));
  }
  
  @Test
  public void writeCaged5ArpeggiosFretboards() {
    writeDeck(new Caged5ArpeggiosFretboards(validator));
  }
  
  private void writeDeck(CardGenerator<FretboardDiagramCard> generator) {
	  ProdUtils.writeTo(new FretboardDiagramDeck(generator), 0.1);
  }
}

