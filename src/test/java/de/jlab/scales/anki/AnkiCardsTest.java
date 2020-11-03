package de.jlab.scales.anki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class AnkiCardsTest {
  AnkiCards anki = new AnkiCards();

  @Test
  public void learnModes() {
    Deck deck = anki.learnModes();
    checkAndWrite(deck);
  }
  
  @Test
  public void playGuitarScales() {
    checkAndWrite(anki.playGuitarScales());
  }
  
  @Test
  public void playScales() {
    checkAndWrite(anki.playScales());
  }
  
  @Test
  public void playGuitarModes() {
    checkAndWrite(anki.playGuitarModes());
  }
  
  @Test
  public void playModes() {
    Deck deck = anki.playModes();
    deck.writeHtml(ScaleCard.TEMPLATE, Paths.get("build/anki"));
    checkAndWrite(deck);
  }
  
  private void checkAndWrite(Deck deck) {
    TestUtils.assertFileContentMatches(deck.getCsv(), AnkiCardsTest.class, deck.getId() + ".txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

}
