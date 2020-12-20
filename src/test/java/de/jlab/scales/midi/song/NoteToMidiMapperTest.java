package de.jlab.scales.midi.song;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
 

}
