package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class PlayChordNotesDeckTest {

  @Test
  public void test() {
    TestUtils.writeTo(new PlayChordNotesDeck(), 0);
  }

}
