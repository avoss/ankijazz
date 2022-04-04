package com.ankijazz.fretboard;

import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.G;

import java.util.List;

import com.ankijazz.theory.Note;

public class StandardTuning implements Tuning {

  public static final int LOW_E_STRING = 0;
  public static final int A_STRING = 1;
  public static final int D_SRING = 2;
  public static final int G_STRING = 3;
  public static final int B_STRING = 4;
  public static final int HIGH_E_STRING = 5;
  
  @Override
  public List<Note> getStrings() {
    return List.of(E, A, D, G, B, E);
  }

  @Override
  public List<Integer> getMidiPitches() {
    return List.of(40, 45, 50, 55, 59, 64);
  }

}
