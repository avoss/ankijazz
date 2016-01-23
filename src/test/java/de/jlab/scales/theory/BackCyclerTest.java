package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class BackCyclerTest {

  @Test
  public void test() {
    BackCycler bc = new BackCycler(Cmaj7);
    bc.add(F).add(G).add(A);
    List<Scale> chords = bc.backcycle();
    assertEquals(4, chords.size());
    for (Scale chord : chords)
      System.out.println(chord.asChord() + " " + chord.asScale());
  }
  
  @Test
  public void test8() {
    BackCycler bc = new BackCycler(Cm7);
    bc.add(G, C).add(Bb, F).add(Eb, G).add(F, C).add(Bb, Eb).add(C, G);
    print(bc);
  }
  
  @Test
  public void testLutz2() {
    BackCycler bc = new BackCycler(Cmaj7.transpose(Bb));
    bc.add(C, Bb, F);
    print(bc);
  }
  
  @Test
  public void testLutz3() {
    BackCycler bc = new BackCycler(Cm7);
    bc.add(G).add(F).add(Eb).add(D);
    print(bc);
  }

  
  @Test
  public void testBembe() {
    BackCycler bc = new BackCycler(Cm7.transpose(A));
    bc.add(C).add(Bb).add(Bb).add(Ab);
    print(bc);
  }

  private void print(BackCycler bc) {
    List<Scale> chords = bc.backcycle();
    for (Scale chord : chords)
      System.out.println(chord.asChord());
  }
}
