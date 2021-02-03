package de.jlab.scales.fretboard2;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.G;

import java.util.List;

import de.jlab.scales.theory.Note;

public interface Tuning {
  static Tuning STANDARD_TUNING = () -> List.of(E, A, D, G, B, E);
  List<Note> getStrings();
  
  static final int LOW_E_STRING = 0;
  static final int A_STRING = 1;
  static final int D_SRING = 2;
  static final int G_STRING = 3;
  static final int B_STRING = 4;
  static final int HIGH_E_STRING = 5;

}
