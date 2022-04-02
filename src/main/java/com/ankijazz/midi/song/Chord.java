package de.jlab.scales.midi.song;

import de.jlab.scales.theory.Scale;
import lombok.Data;

/**
 * Chord contains a Scale (what sound to produce) and a symbol (what to write). 
 * This is because you may want to write a Cm7 chords as "Cm7" or "C-7" or mixed.
 */

@Data
public class Chord {
  private final Scale scale;
  private final String symbol;

  public static Chord of(Scale scale, String symbol) {
    return new Chord(scale, symbol);
  }

}
