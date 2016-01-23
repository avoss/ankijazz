package de.jlab.scales.algo;

import org.junit.Test;

import de.jlab.scales.midi.Drum;
import de.jlab.scales.midi.MidiFile;

public class DefaultInstrumentTest {

  @Test
  public void test() {
    MidiFile mf = new MidiFile();
    Instrument instrument = Instruments.drum(Drum.ClosedHiHat).rhythm("x.7.7.7. x2738495").length("x").velocity(60, 80).build();
    instrument.play(1600).perform(mf);
    mf.save("temp/test.midi");
    
  }

}
