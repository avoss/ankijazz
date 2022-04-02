package com.ankijazz.midi.song;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.Utils;
import com.ankijazz.midi.song.Song;
import com.ankijazz.midi.song.ProgressionFactory.ChordProgressionSet;
import com.ankijazz.midi.song.ProgressionFactory.Progression;
import com.ankijazz.midi.song.ProgressionFactory.ProgressionSet;
import com.ankijazz.theory.Note;

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
    assertEquals("| Gbm | Bm | E | Am |", minorSong.toString());
    assertTrue(minorProgression.isMinor());
    
  }

}
