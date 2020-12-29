package de.jlab.scales.midi.song;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.midi.song.ProgressionFactory.ProgressionSet;
import de.jlab.scales.theory.Note;

public class ProgressionFactoryTest {

  @Test
  public void testLoadYaml() {
    ProgressionFactory factory = new ProgressionFactory(Utils.fixedLoopIteratorFactory());
    ProgressionSet set = factory.load("triads.yaml");
    assertEquals(5, set.getProgressions().size());
    Song song = new Song(set.getProgressions().get(0).create(TestUtils.majorKeySignature(Note.C)));
    assertEquals("| A | D | G | C |", song.toString());
  }

}
