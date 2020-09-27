package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

/**
 * triples cards: play on low, medium, high positions on fretboard
 */

public class GuitarDeck implements Deck {
  private final Deck delegate;

  public GuitarDeck(Deck delegate) {
    this.delegate = delegate;
  }

  @Override
  public void add(Card card) {
    delegate.add(new CardDecorator(card, "Low", "<img src=\"AnkiJazz-FretboardLow.png\">"));
    delegate.add(new CardDecorator(card, "Medium", "<img src=\"AnkiJazz-FretboardMedium.png\">"));
    delegate.add(new CardDecorator(card, "High", "<img src=\"AnkiJazz-FretboardHigh.png\">"));
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public void writeTo(Path dir) {
    delegate.writeTo(dir);
  }

  @Override
  public void shuffle(int randomness) {
    delegate.shuffle(randomness);
  }

  @Override
  public List<String> getCsv() {
    return delegate.getCsv();
  }

  @Override
  public List<Card> getCards() {
    return delegate.getCards();
  }

  @Override
  public void writeHtml(Path dir) {
    delegate.writeHtml(dir);
  }
}
