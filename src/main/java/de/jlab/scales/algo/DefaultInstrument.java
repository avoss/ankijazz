package de.jlab.scales.algo;

import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.rest;

import de.jlab.scales.midi.Parallel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.ProgramChange;
import de.jlab.scales.midi.Sequential;

public class DefaultInstrument extends AbstractInstrument {
  
  int playing;

  public Part play(int songLengthInMicrotimeUnits) {
    reset();
    Sequential seq = new Sequential();
    playProgramChange(seq);
    for (int i = 0; i < songLengthInMicrotimeUnits; i++) {
      seq.add(playMicrotimeUnit());
      seq.add(rest(1, denominator));
    }
    return seq;
  }
  
  @Override
  protected void reset() {
    playing = 0;
    super.reset();
  }

  private void playProgramChange(Sequential seq) {
    if (program != null)
      seq.add(new ProgramChange(midiChannel, program));
  }

  private Part playMicrotimeUnit() {
    Parallel par = new Parallel();
    
    if (!rhythmGenerator.next())
      return par;
    
    if (playing > 0) {
      playing -= 1;
      return par;
    }

    int length = lengthGenerator.next();
    int velocity = velocityGenerator.next();
    for (int pitch : pitchGenerator.next())
      par.add(note(midiChannel, pitch, velocity, length, denominator));
    this.playing = length - 1;
    return par;
  }
}
