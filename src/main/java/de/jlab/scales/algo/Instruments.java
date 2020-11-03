package de.jlab.scales.algo;

import de.jlab.scales.midi.Drum;
import de.jlab.scales.random.ChooseAny;
import de.jlab.scales.random.Converter;
import de.jlab.scales.random.Sequence;

public class Instruments {
  private Instruments() {
  }

  public static InstrumentBuilder drum(Drum ... drums) {
    Sequence<Drum> drumChooser = new ChooseAny<Drum>(drums);
    Sequence<int[]> pitchGenerator = new Converter<int[], Drum>(drumChooser) {
      @Override
      protected int[] convert(Drum dice) {
        return new int[]{dice.getMidiPitch()};
      }
    };
    return new InstrumentBuilder(Drum.getMidiChannel(), pitchGenerator);
  }

}
