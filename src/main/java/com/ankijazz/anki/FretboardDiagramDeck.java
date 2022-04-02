package de.jlab.scales.anki;

public class FretboardDiagramDeck extends AbstractDeck<FretboardDiagramCard> {

  public FretboardDiagramDeck(CardGenerator<FretboardDiagramCard> generator) {
    super(generator.getTitle(), generator.getFileName());
    for (FretboardDiagramCard card : generator.generate()) {
      super.add(card);
    }
  }

}
