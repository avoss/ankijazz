package de.jlab.scales.anki;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class JamDeckTest {

  @Test
  public void test() {
    JamDeck deck = new JamDeck("Test Jam Deck");
    TestUtils.writeTo(deck, 0.2);
    assertDeckContainsNoDuplicates(deck);
  }

  private void assertDeckContainsNoDuplicates(JamDeck deck) {
    Set<String> assetIds = new HashSet<>();
    for (JamCard card : deck.getCards()) {
      String assetId = card.getAssetId();
      if (assetIds.contains(assetId)) {
        fail("duplicate asset id: " + assetId);
      }
      assetIds.add(assetId);
    }
  }

}
