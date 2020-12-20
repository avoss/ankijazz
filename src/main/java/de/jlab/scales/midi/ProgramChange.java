package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class ProgramChange extends AbstractPart {
  
  private int channel;
  private Program program;

  public ProgramChange(int channel, Program program) {
    this.channel = channel;
    this.program = program;

  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.programChange(channel, program.getMidiProgram());
  }

}
