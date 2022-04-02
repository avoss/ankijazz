package com.ankijazz.midi.song;

import static com.ankijazz.Utils.getLast;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Scales.C7;
import static com.ankijazz.theory.Scales.C9;
import static com.ankijazz.theory.Scales.allKeys;
import static com.ankijazz.theory.Scales.allModes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.ankijazz.midi.MidiUtils;
import com.ankijazz.midi.song.Drop2ChordGenerator;
import com.ankijazz.theory.BuiltinChordType;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleType;
import com.ankijazz.theory.Stacker;

public class Drop2ChordGeneratorTest {

  final int highestMidiPitch = 72;
  final Note highestMidiNote = MidiUtils.midiPitchToNote(highestMidiPitch);
  final Drop2ChordGenerator generator = new Drop2ChordGenerator(highestMidiPitch);
  
  @Test
  public void testLimitNumberOfNotes() {
    List<Note> notes = new Stacker(C9).getStackedThirds();
    assertEquals(List.of(C, E, G, Bb, D), notes);
    assertEquals(List.of(E, G, Bb, D), generator.limitNumberOfNotes(C, notes));
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
