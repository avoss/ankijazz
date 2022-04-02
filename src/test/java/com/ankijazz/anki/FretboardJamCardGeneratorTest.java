package com.ankijazz.anki;

import static com.ankijazz.anki.FretboardJamCardGenerator.CAGED_MODES;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.Utils;
import com.ankijazz.anki.FretboardJamCardGenerator;
import com.ankijazz.anki.FretboardJamCardGenerator.PlayThroughChangesMelody;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.song.Ensembles;
import com.ankijazz.midi.song.Song;
import com.ankijazz.sheet.PngImageRenderer;
import com.ankijazz.sheet.RenderContext;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.Scales;

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
  public void testMelody() {
    FretboardJamCardGenerator generator = new FretboardJamCardGenerator(CAGED_MODES, Utils.randomLoopIteratorFactory());
    PlayThroughChangesMelody melody = generator.new PlayThroughChangesMelody();
    Scale cmajor = Scales.CMajor;
    Scale bmajor = cmajor.transpose(B);
    melody.start(cmajor);
    assertEquals("[C, B, A, G, F, E, D]", melody.createNoteList(cmajor).toString());
    melody.next();
    melody.next();
    assertEquals(G, melody.prevNote);
    melody.start(bmajor);
    assertEquals("[Gb, E, Eb, Db, B, Bb, Ab]", melody.createNoteList(bmajor).toString());
    melody.next();
    melody.next();
    melody.start(cmajor);
    assertTrue(melody.ascending);
    assertEquals("[C, D, E, F, G, A, B]", melody.createNoteList(cmajor).toString());
    melody.next();
    melody.next();
    assertEquals(F, melody.prevNote);
    melody.start(bmajor);
    assertEquals("[Gb, Ab, Bb, B, Db, Eb, E]", melody.createNoteList(bmajor).toString());
  }

  @Test
  public void testMidiMatches() {
    FretboardJamCardGenerator generator = new FretboardJamCardGenerator(CAGED_MODES, Utils.fixedLoopIteratorFactory());
    Song song = generator.songFactory.next().getSong();
    Part part = Ensembles.latin(120).play(song, 2);
    BufferedImage image = new PngImageRenderer(RenderContext.ANKI, song).render();
    TestUtils.assertMidiMatches(part, getClass(), "FretboardJamCardGeneratorTest.midi");
    TestUtils.assertImageMatches(image, getClass(), "FretboardJamCardGeneratorTest.png");
  }


}
