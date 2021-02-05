package de.jlab.scales.anki;

import static de.jlab.scales.anki.FretboardDiagramDeck.Options.RenderMode.*;

import java.util.ArrayList;
import java.util.List;

import de.jlab.scales.fretboard2.Fingering;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class FretboardDiagramDeck extends AbstractDeck<FretboardDiagramCard> {
  

  @lombok.Builder
  @lombok.Data
  public static class Options {
    public enum RenderMode { SCALE_ONLY, ARPEGGIO_ONLY, SCALE_AND_ARPEGGIO }
    public enum ArpeggioMode { ARPEGGIO, PENTATONIC }
    private final String title;
    private final String fileName;
    private final RenderMode renderMode;
    private final ArpeggioMode arpeggioMode;
    private final int numberOfCards;
    private final String chords;
  }
 

  public FretboardDiagramDeck(CardGenerator<FretboardDiagramCard> generator) {
    super(generator.getTitle(), generator.getFileName());
    for (FretboardDiagramCard card : generator.generate()) {
      super.add(card);
    }
  }

}
