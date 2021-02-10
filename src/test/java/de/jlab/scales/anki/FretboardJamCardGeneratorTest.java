package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.SongWrapper;

public class FretboardJamCardGeneratorTest {

  @Test
  public void test() {
    FretboardJamCardGenerator generator = new FretboardJamCardGenerator("Test", "FretboardJam", Utils.fixedLoopIteratorFactory());
    SongWrapper wrapper = generator.songFactory.next();
    Part part = Ensembles.latin(130).play(wrapper.getSong(), 2);
    TestUtils.assertMidiMatches(part, getClass(), "FretboardJamCardGeneratorTest.midi");
    
  }

}
