package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;
import static org.junit.Assert.fail;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

public class JamDeckTest {

  @Test
  public void test() {
    //JamDeck deck = new JamDeck("Test Guitar", Note.C, true);
    JamDeck deck = new JamDeck("Test", Note.C, false);
    TestUtils.writeTo(deck, 0.2);
  }

}
