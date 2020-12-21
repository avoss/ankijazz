package de.jlab.scales.midi.song;

import static de.jlab.scales.theory.Scales.Cmaj7;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.jlab.scales.midi.Drum;
import de.jlab.scales.midi.Parallel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;
import de.jlab.scales.midi.Sequential;
import de.jlab.scales.midi.Tempo;
import de.jlab.scales.midi.TimeSignature;

public class Ensemble {
  
  private final List<Instrument> instruments = new ArrayList<>();
  private final TimeSignature timeSignature;
  private final Tempo tempo;
  private final int ticksPerBar;
  private final int beatsPerBar;
  
  private int midiChannel = -1;
  private int drumVolume = 100;
  private int drumPan = 0;
  private Optional<Part> countInPart = Optional.empty();
  
  public Ensemble(int ticksPerBar, TimeSignature timeSignature, Tempo tempo) {
    this.ticksPerBar = ticksPerBar;
    this.timeSignature = timeSignature;
    this.beatsPerBar = timeSignature.getNumerator();
    this.tempo = tempo;
  }
  
  public PercussiveInstrument percussive(Drum drum) {
    PercussiveInstrument instrument = new PercussiveInstrument(beatsPerBar, ticksPerBar, drum);
    instruments.add(instrument);
    return instrument;
  }

  public void countIn(Drum drum, String pattern) {
    PercussiveInstrument instrument = new PercussiveInstrument(beatsPerBar, ticksPerBar, drum);
    instrument.bar(pattern);
    countInPart = Optional.of(instrument.play(Song.of(Bar.of(Chord.of(Cmaj7, "Cmaj7")))));
  }

  public MonophonicInstrument monophonic(int lowestMidiPitch, Program program, int volume, int pan) {
    return monophonic(NoteToMidiMapper.octave(lowestMidiPitch), program, volume, pan);
  }
  
  public MonophonicInstrument monophonic(NoteToMidiMapper noteToMidiMapper, Program program, int volume, int pan) {
    MonophonicInstrument instrument = new MonophonicInstrument(beatsPerBar, ticksPerBar, nextMidiChannel(), noteToMidiMapper, program, volume, pan);
    instruments.add(instrument);
    return instrument;
  }

  public PolyphonicInstrument drop2chords(int lowestMidiPitch, Program program, int volume, int pan) {
    ChordToMidiMapper mapper = new Drop2ChordGenerator(lowestMidiPitch);
    PolyphonicInstrument instrument = new PolyphonicInstrument(beatsPerBar, ticksPerBar, nextMidiChannel(), mapper, program, volume, pan);
    instruments.add(instrument);
    return instrument;
  }
  
  public Part play(Song song, int repeatCount) {
    Sequential sequential = new Sequential(); 
    sequential.add(timeSignature, tempo);
    sequential.add(Parts.volume(Drum.getMidiChannel(), drumVolume));
    sequential.add(Parts.pan(Drum.getMidiChannel(), drumPan));
    if (countInPart.isPresent()) {
      sequential.add(countInPart.get());
    }
    Parallel parallel = new Parallel();
    for (Instrument instrument : instruments) {
      parallel.add(instrument.play(song));
    }
    sequential.add(parallel.repeat(repeatCount));
    return sequential;
  }

  /**
   * all percussive instruments share same midi channel, so cannot be configured individually
   */
  public void setDrumVolume(int volume) {
    this.drumVolume = volume;
  }

  public void setDrumPan(int pan) {
    this.drumPan = pan;
  }

  private int nextMidiChannel() {
    midiChannel += 1;
    if (midiChannel == Drum.getMidiChannel()) {
      midiChannel += 1;
    }
    if (midiChannel > 15) {
      throw new IllegalStateException("midi has only 16 midi channels");
    }
    return midiChannel;
  }


}
