package de.jlab.scales.anki;

import static de.jlab.scales.Utils.fixedLoopIteratorFactory;
import static de.jlab.scales.anki.FretboardJamCardGenerator.CAGED_MODES;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.Utils;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.Note;

public class FretboardJamCardGeneratorTest {
  
  @Test
  public void assertThatChordRootsAreAtLeast2SemitonesApart() {
    FretboardJamCardGenerator generator = new FretboardJamCardGenerator(CAGED_MODES, Utils.randomLoopIteratorFactory());
    for (int i = 0; i < 100; i++) {
      Song song = generator.songFactory.next().getSong();
      assertThatChordRootsAreAtLeast2SemitonesApart(generator, song);
    }
  }

  private void assertThatChordRootsAreAtLeast2SemitonesApart(FretboardJamCardGenerator generator, Song song) {
    List<Note> roots = song.getBars().stream().flatMap(bar -> bar.getChords().stream()).map(chord -> chord.getScale().getRoot()).collect(toList());
    Note prevRoot = null;
    for (int i = 0; i < roots.size(); i += generator.numberOfBarsPerChord) {
      Note root = roots.get(i);
      if (prevRoot != null) {
        assertTrue(prevRoot.distance(root) > 1);
      }
      prevRoot = root;
    }
  }

  @Test
  public void testMidiMatches() {
    Part part = Ensembles.latin(130).play(song(), 2);
    TestUtils.assertMidiMatches(part, getClass(), "FretboardJamCardGeneratorTest.midi");
    
  }

  private Song song() {
    FretboardJamCardGenerator generator = new FretboardJamCardGenerator(CAGED_MODES, fixedLoopIteratorFactory());
    Song song = generator.songFactory.next().getSong();
    return song;
  }

}
