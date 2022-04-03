package com.ankijazz.midi.song;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scales;

public class IntervalToChordIndexMapperTest {

  @Test
  public void testIndex() {
    IntervalToChordIndexMapper mapper = new IntervalToChordIndexMapper();
    assertEquals(0, mapper.map(1));
    assertEquals(1, mapper.map(3));
    assertEquals(2, mapper.map(5));
    assertEquals(3, mapper.map(7));
    assertEquals(4, mapper.map(9));
  }
  
  @Test
  public void testNote() {
    IntervalToChordIndexMapper mapper = new IntervalToChordIndexMapper();
    assertEquals(Note.G, mapper.map(Scales.Cm11, 5));
    assertEquals(Note.Gb, mapper.map(Scales.C7flat5, 5));
    assertEquals(Note.Ab, mapper.map(Scales.C7sharp5, 5));
    assertEquals(Note.E, mapper.map(Scales.C7, 4));
    assertEquals(Note.C, mapper.map(Scales.CmajTriad, 7));
    
  }

}
