package de.jlab.scales.midi.song;

import de.jlab.scales.midi.Drum;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;

public class Ensembles {
  private Ensembles() {}
  
  public static Ensemble latin(int tempo) {
    Ensemble ensemble = new Ensemble(16, Parts.timeSignature(4, 4), Parts.tempo(tempo));
    ensemble.setDrumVolume(90);
    ensemble.setDrumPan(-20);
    ensemble.countIn(Drum.Cowbell, "x... x... x... x...");
    ensemble.percussive(Drum.BassDrum1)
      .bar("9... .... ..9. ....")
      .bar("..9. .... .... ....");

    ensemble.percussive(Drum.AcousticSnare)
      .bar(".... 9.56 .... ....")
      .bar(".... 9... .... ....");

    ensemble.percussive(Drum.LowMidTom)
      .bar(".... .... .... ....")
      .bar(".... .... .... ....")
      .bar(".... .... .... ....")
      .bar(".... .... .... ..x.");

    ensemble.percussive(Drum.HighTom)
      .bar(".... .... .... ...8")
      .bar(".... .... ...6 .7.8")
      .bar(".... .... .... ....")
      .bar(".... ..x. .... ....");

    ensemble.percussive(Drum.CrashCymbal1)
      .bar("5... .... .... ....")
      .bar(".... .... .... ....")
      .bar(".... 3... .... ....")
      .bar(".... .... .... ....");

    ensemble.percussive(Drum.RideCymbal1)
      .bar("6.7. 6.7. 6.7. 6.7.")
      .bar("6.7. 6.7. 6.7. 6.7.");

    ensemble.percussive(Drum.ClosedHiHat)
      .bar(".... .... .... 7...")
      .bar(".... .... 6... 6...");

    ensemble.monophonic(32, Program.FretlessBass, 105, -40)
      .bar("8--- -.8. 8---", 1, 5, 5)
      .bar("..8. 8--- -.8. 8--- ....", 1, 1, 5, 5);

    ensemble.drop2chords(55, Program.ElectricPiano1, 60, 0)
      .bar("8... 8--- ..8.")
      .bar("..8- ---- ..8. ..8- ---.");

    return ensemble;
  }
  
  public static Ensemble funk(int tempo) {
    Ensemble ensemble = new Ensemble(16, Parts.timeSignature(4, 4), Parts.tempo(tempo));
    ensemble.setDrumVolume(75);
    ensemble.setDrumPan(-20);
    ensemble.countIn(Drum.Cowbell, "x... x... x... x...");
    ensemble.percussive(Drum.BassDrum1)
      .bar("x..x ..x. ..x. ..x.")
      .bar("x..x ..x. .... ....");

    ensemble.percussive(Drum.AcousticSnare)
      .bar(".... x... .... ....")
      .bar(".... x... .... x.xx");

    ensemble.percussive(Drum.ClosedHiHat)
      .bar("x... ..x. .x.. x...")
      .bar("x7x. .x7x 6x6. .x..");

    ensemble.percussive(Drum.OpenHiHat)
      .bar(".... .... .... ....")
      .bar(".... .... ..9. ....");

    ensemble.percussive(Drum.CrashCymbal2)
      .bar("8... .... .... ....")
      .bar(".... .... .... ....")
      .bar(".... .... .... ....")
      .bar(".... .... .... ....");
    
    ensemble.monophonic(32, Program.FingeredElectricBass, 127, 30)
      .bar("9--- ..8x .... ....", 1, 1, 1)
      .bar("9--- ...9 ---- ....", 1, 1);

    ensemble.drop2chords(55, Program.ElectricPiano2, 55, -1)
      .bar("x..x .... ...x ..x.")
      .bar("x--- ...x ---- ....");

    return ensemble;
  }
  
}
