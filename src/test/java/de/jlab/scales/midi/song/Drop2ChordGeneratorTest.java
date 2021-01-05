package de.jlab.scales.midi.song;

import static de.jlab.scales.Utils.getLast;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C9;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.allModes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.midi.MidiUtils;
import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleType;

public class Drop2ChordGeneratorTest {

  final int highestMidiPitch = 72;
  final Note highestMidiNote = MidiUtils.midiPitchToNote(highestMidiPitch);
  final Drop2ChordGenerator generator = new Drop2ChordGenerator(highestMidiPitch);
  
  @Test
  public void testLimitNumberOfNotes() {
    List<Note> notes = generator.stackedThirds(C9);
    assertEquals(List.of(C, E, G, Bb, D), notes);
    assertEquals(List.of(E, G, Bb, D), generator.limitNumberOfNotes(C, notes));
  }
  
  @Test
  public void testStackedThirds() {
    for (ScaleType type : BuiltinChordType.values()) {
      Scale prototype = type.getPrototype();
      for (Scale scale : allModes(allKeys(prototype))) {
        Note[] thirds = generator.stackedThirds(scale).toArray(new Note[0]);
        for (int i = 1; i < thirds.length; i++) {
          assertThat(thirds[i-1].distance(thirds[i])).isLessThan(7);
        }
      }
    }
  }
  
  @Test
  public void testDrop2Chord() {
    int[] actual = generator.midiChord(C7);
    int[] expected = new int[] {72, 67, 64, 58};
    assertThat(actual).isEqualTo(expected);
  }
  
  @Test
  public void testDropIndex() {
    assertEquals(0, generator.dropIndex(List.of(F)));
    assertEquals(0, generator.dropIndex(List.of(C, G)));
    assertEquals(1, generator.dropIndex(List.of(C, E, G)));
    assertEquals(2, generator.dropIndex(List.of(C, E, G, Bb)));
    assertEquals(2, generator.dropIndex(List.of(C, E, G, Bb, Db)));
  }
  
  @Test
  public void testFindBestInversion() {
    assertThat(generator.findBestInversion(List.of(F))).isEqualTo((List.of(F)));
    assertThat(generator.findBestInversion(List.of(G, C))).isEqualTo((List.of(G, C)));
    assertThat(generator.findBestInversion(List.of(C, G))).isEqualTo((List.of(G, C)));
    assertThat(generator.findBestInversion(List.of(C, E, G))).isEqualTo((List.of(E, G, C)));
    assertThat(generator.findBestInversion(List.of(G, B, C, E))).isEqualTo((List.of(E, G, B, C)));
    assertThat(generator.findBestInversion(List.of(G, B, D, E))).isEqualTo((List.of(D, E, G, B)));

    for (ScaleType type : BuiltinChordType.values()) {
      Scale prototype = type.getPrototype();
      for (Scale scale : allModes(allKeys(prototype))) {
        List<Note> best = generator.findBestInversion(scale.asList());
        assertThat(getLast(best).distance(highestMidiNote)).isLessThan(7);
      }
    }
    
    
    List<Note> list = new ArrayList<>(List.of(C, E, G, B));
    for (int i = 0; i < list.size(); i++) {
      Collections.rotate(list, 1);
      assertThat(generator.findBestInversion(list)).isEqualTo((List.of(E, G, B, C)));
    }
  }

}
