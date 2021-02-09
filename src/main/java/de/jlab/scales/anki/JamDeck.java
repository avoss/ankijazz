package de.jlab.scales.anki;

public class JamDeck extends AbstractDeck<JamCard> {
  public JamDeck(CardGenerator<JamCard> generator) {
    super(generator.getTitle(), generator.getFileName());
    getCards().addAll(generator.generate());
  }


 
}
