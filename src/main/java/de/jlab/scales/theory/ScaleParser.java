package de.jlab.scales.theory;

import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;

public class ScaleParser {

  private static ScaleUniverse universe = new ScaleUniverse(Major, MelodicMinor, HarmonicMinor);

  // TODO duplicated in ScaleUniverse
  public static String asScale(Scale scale) {
    Scale superScale = universe.info(scale).getSuperScales().stream().findFirst().orElse(scale);
    return scale.asScale(Accidental.fromScale(superScale));
  }

}
