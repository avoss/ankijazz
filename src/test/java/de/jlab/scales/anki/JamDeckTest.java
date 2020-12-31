package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;
import static org.junit.Assert.fail;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class JamDeckTest {

  @Test
  public void test() {
    JamDeck deck = new JamDeck("Test Jam Deck", EnumSet.of(Workouts, AllKeys));
    TestUtils.writeTo(deck, 0.2);
    assertDeckContainsNoDuplicates(deck);
  }
  
  private void assertDeckContainsNoDuplicates(JamDeck deck) {
    Map<String, Card> assetIds = new HashMap<>();
    for (JamCard card : deck.getCards()) {
      String assetId = card.getAssetId();
      if (assetIds.containsKey(assetId)) {
        fail("duplicate asset id: " + assetId);
      }
      assetIds.put(assetId, card);
    }
  }

}
