package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.TestUtils;

public class ChordNoteTrainerDeckTest {

  @Test
  public void testDeckContent() {
    TestUtils.assertFileContentMatches(new ChordNoteTrainerDeck().getJson(), getClass(), "ChordNoteTrainerDeck.json");
  }

}
