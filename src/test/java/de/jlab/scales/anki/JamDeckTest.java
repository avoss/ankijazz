package de.jlab.scales.anki;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class JamDeckTest {

  @Test
  public void test() {
    JamDeck deck = new JamDeck("Test Jam Deck");
    assertDeckContainsNoDuplicates(deck);
    TestUtils.writeTo(deck, 0.2);
  }

  private void assertDeckContainsNoDuplicates(JamDeck deck) {
    Set<String> set = deck.getCards().stream().map(c -> c.assetId()).collect(toSet());
    assertEquals(deck.getCards().size(), set.size());
  }

}
