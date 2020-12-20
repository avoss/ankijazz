package de.jlab.scales.midi.song;

import de.jlab.scales.theory.Scale;
import lombok.Data;

@Data
public class Chord {
  private final Scale scale;
  private final String symbol;

  public static Chord of(Scale scale, String symbol) {
    return new Chord(scale, symbol);
  }

}
