package com.ankijazz.midi.song;

import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.F;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ankijazz.midi.song.NoteToMidiMapper;

public class NoteToMidiMapperTest {
  

  @Test
  public void testNextClosest() {
    NoteToMidiMapper mapper = NoteToMidiMapper.range(50, 70);
    assertEquals(60, mapper.nextClosest(C));
    assertEquals(61, mapper.nextClosest(Db));
    assertEquals(58, mapper.nextClosest(Bb));
    assertEquals(64, mapper.nextClosest(E));
    assertEquals(69, mapper.nextClosest(A));
    assertEquals(60, mapper.nextClosest(C));
    assertEquals(57, mapper.nextClosest(A));
    assertEquals(53, mapper.nextClosest(F));
    assertEquals(50, mapper.nextClosest(D));
    assertEquals(60, mapper.nextClosest(C));
  }
  
  @Test
  public void testNextHigher() {
    NoteToMidiMapper mapper = NoteToMidiMapper.octave(60);
    assertEquals(69, mapper.nextHigher(A));
    assertEquals(62, mapper.nextHigher(D));
  }
 
  @Test
  public void testNextHigherUnbounded() {
    NoteToMidiMapper mapper = NoteToMidiMapper.octave(60);
    assertEquals(69, mapper.nextHigherUnbounded(A));
    assertEquals(74, mapper.nextHigherUnbounded(D));
  }

  @Test
  public void testNextLower() {
    NoteToMidiMapper mapper = NoteToMidiMapper.octave(60);
    assertEquals(62, mapper.nextLower(D));
    assertEquals(61, mapper.nextLower(Db));
    assertEquals(69, mapper.nextLower(A));
  }
  
  @Test
  public void testNextLowerUnbounded() {
    NoteToMidiMapper mapper = NoteToMidiMapper.octave(60);
    assertEquals(62, mapper.nextLowerUnbounded(D));
    assertEquals(61, mapper.nextLowerUnbounded(Db));
    assertEquals(57, mapper.nextLowerUnbounded(A));
  }
  
}
