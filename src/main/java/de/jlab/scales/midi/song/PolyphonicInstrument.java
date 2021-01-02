package de.jlab.scales.midi.song;

import java.util.function.BiFunction;

import de.jlab.scales.midi.Parallel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;
import de.jlab.scales.theory.Scale;

public class PolyphonicInstrument extends TonalInstrument<PolyphonicInstrument> {

  private ChordGenerator chordGenerator;
  private int denominator;
  private int midiChannel;

  public PolyphonicInstrument(int beatsPerBar, int ticksPerBar, int midiChannel, ChordGenerator chordGenerator, Program program, int volume, int pan) {
    super(beatsPerBar, ticksPerBar, midiChannel, program, volume, pan);
    this.denominator = ticksPerBar;
    this.midiChannel = midiChannel;
    this.chordGenerator = chordGenerator;
  }
  
  public PolyphonicInstrument bar(String pattern) {
    super.parse(pattern);
    return this;
  }


  @Override
  protected BiFunction<Event, Scale, Part> getPlayer() {
    return (event, chord) -> {
      Parallel container = new Parallel();
      int[] midiPitches = chordGenerator.midiChord(chord);
      for (int midiPitch : midiPitches) {
        container.add(Parts.note(midiChannel, midiPitch, event.getVelocity(), event.getNoteLength(), denominator));
      }
      return container;
    };
  }

}
