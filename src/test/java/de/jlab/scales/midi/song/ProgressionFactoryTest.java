package de.jlab.scales.midi.song;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.midi.song.ProgressionFactory.ChordProgressionSet;
import de.jlab.scales.midi.song.ProgressionFactory.Progression;
import de.jlab.scales.midi.song.ProgressionFactory.ProgressionSet;
import de.jlab.scales.theory.Note;

public class ProgressionFactoryTest {

  @Test
  public void testLoadYaml() {
    ProgressionSet set = ChordProgressionSet.loadChordProgression("Triads.yaml", Utils.fixedLoopIteratorFactory());
    assertEquals(5, set.getProgressions().size());
    
    Progression majorProgression = set.getProgressions().get(0);
    Song majorSong = new Song(majorProgression.create(TestUtils.majorKeySignature(Note.C)));
    assertEquals("| A | D | G | C |", majorSong.toString());
    assertFalse(majorProgression.isMinor());
    
    Progression minorProgression = set.getProgressions().get(1);
    Song minorSong = new Song(minorProgression.create(TestUtils.majorKeySignature(Note.C)));
    assertEquals("| Am | Dm | G | Cm |", minorSong.toString());
    assertTrue(minorProgression.isMinor());
    
  }

}
