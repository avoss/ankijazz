package de.jlab.scales.midi.song;

import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.theory.Scale;

public class Drop2ChordGeneratorTest {

  Drop2ChordGenerator generator = new Drop2ChordGenerator(60);
  
  @Test
  public void testChord() {
    int[] actual = generator.midiChord(C7);
    int[] expected = new int[] {60, 67, 70, 76};
    assertArrayEquals(expected, actual);
    
  }
  
  @Test
  public void testDropIndex() {
    assertEquals(0, generator.dropIndex(new Scale(F)));
    assertEquals(0, generator.dropIndex(new Scale(C, G)));
    assertEquals(1, generator.dropIndex(CmajTriad));
    assertEquals(2, generator.dropIndex(C7));
    assertEquals(2, generator.dropIndex(C7flat9));
  }
  
  @Test
  public void testFindBestInversion() {
    assertEquals(new Scale(F), generator.findBestInversion(new Scale(F), 0));
    assertEquals(new Scale(C, G), generator.findBestInversion(new Scale(C, G), 0));
    assertEquals(new Scale(C, G), generator.findBestInversion(new Scale(G, C), 0));
    assertEquals(CmajTriad.superimpose(G), generator.findBestInversion(CmajTriad, 1));
    assertEquals(C7.superimpose(G), generator.findBestInversion(C7, 2));
    assertEquals(new Scale(G, Bb, Db, E), generator.findBestInversion(C7flat9, 2));
  }

}
