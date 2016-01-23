package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.midi.Drum;

public class ConverterTest {

  @Test
  public void test() {
    FixedSequence<Drum> hihat = new FixedSequence<Drum>(Drum.ClosedHiHat);
    Converter<Integer, Drum> converter = new Converter<Integer, Drum>(hihat) {

      @Override
      protected Integer convert(Drum drum) {
        return drum.getMidiPitch();
      }

      
    };
    assertEquals(Drum.ClosedHiHat.getMidiPitch(), converter.next().intValue());
  }

}
