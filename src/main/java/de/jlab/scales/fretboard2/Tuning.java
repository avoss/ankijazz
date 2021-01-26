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

}
