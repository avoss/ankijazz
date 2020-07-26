package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
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
      System.out.println(chord.asChord() + " " + chord.asScale(FLAT));
  }
  
  @Test
  public void testBeatPath() {
    BackCycler bc = new BackCycler(Cmaj7.transpose(3));
    bc.add(D, Eb, F).add(G).add(F, G, Ab);//.add(Bb);
    //bc.add(D).add(Eb, F);
    print(bc);
  }
  
  private void print(BackCycler bc) {
    List<Scale> chords = bc.backcycle();
    for (Scale chord : chords)
      System.out.println(chord.asChord() +  " = " + chord.asScale(FLAT));
  }
}
