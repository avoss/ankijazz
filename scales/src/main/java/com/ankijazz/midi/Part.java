package com.ankijazz.midi;

public interface Part {
  void perform(MidiOut midiOut);

  Part repeat(int times);

  Part append(Part... parts);

  Part merge(Part... parts);

}
