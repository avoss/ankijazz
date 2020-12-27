package de.jlab.scales.midi.song;

import de.jlab.scales.midi.Drum;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;

/**
 * bar() notation
 * <dl>
 *   <dt>0-9</dt>
 *   <dd>play chord or note with velocity between 0 and ~100</dd
 *   <td>x</dt>
 *   <dd>play chord or note with max velocity of 127</dd>
 *   <dt>.</dt>
 *   <dd>rest, or no sound</dd>
 *   <dt>-</dt>
 *   <dd>increase length of previous sound. E.g. <b>x--</b> will create a note/chord with velocity of 127 and length of 3 ticks</dd>
 *   <dt>&gt;</dt>
 *   <dd>take chord from next beat instead of current beat. Sometimes a note or chord plays the sound of the next beat a little bit before that beat. Does not advance the clock (e.g. <b>x... x...</b> and  <b>x... &gt; x...</b>  have the same rhythmic structure (only notes/chords to pick are different)</dd> 
 *   <dt>everything else</dt>
 *   <dd>is ignored</dd>
 *  </dl>
 */
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
      .bar("8--- > -.8. 8--- > ..8.", 1, 5, 5, 1)
      .bar("8--- -.8. 8--- ....",  1, 5, 5);

    ensemble.drop2chords(55, Program.ElectricPiano1, 60, 0)
      .bar("8... 8--- ..8. > ..8-")
      .bar("---- > ..8. ..8- ---.");

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
      .bar("9--- > ..8x .... ....", 1, 1, 1)
      .bar("9--- ...9 ---- ....", 1, 1);

    ensemble.drop2chords(55, Program.ElectricPiano2, 50, -1)
      .bar("x..x .... x--x ..x.")
      .bar("x--- ...x ---- ....");

    return ensemble;
  }
  
}
