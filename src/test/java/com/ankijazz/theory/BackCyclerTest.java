package com.ankijazz.theory;

import static com.ankijazz.theory.Accidental.FLAT;
import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Ab;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Scales.Cmaj7;
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
