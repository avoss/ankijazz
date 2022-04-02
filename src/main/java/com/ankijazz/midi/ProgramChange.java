package com.ankijazz.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
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
