package com.ankijazz.midi.song;

import static com.ankijazz.theory.Scales.Cmaj7;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ankijazz.midi.Drum;
import com.ankijazz.midi.Parallel;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.midi.Program;
import com.ankijazz.midi.Sequential;
import com.ankijazz.midi.Tempo;
import com.ankijazz.midi.TimeSignature;

public class Ensemble {
  
  private final List<Instrument> instruments = new ArrayList<>();
  private final TimeSignature timeSignature;
  private final Tempo tempo;
  private final int ticksPerBar;
  private final int beatsPerBar;
  private final String style;
  
  private int midiChannel = -1;
  private int drumVolume = 100;
  private int drumPan = 0;
  private Optional<Part> countInPart = Optional.empty();
  
  public Ensemble(String style, int ticksPerBar, TimeSignature timeSignature, Tempo tempo) {
    this.style = style;
    this.ticksPerBar = ticksPerBar;
    this.timeSignature = timeSignature;
    this.beatsPerBar = timeSignature.getNumerator();
    this.tempo = tempo;
  }

  public MelodyInstrument melody(Program program, int volume) {
    MelodyInstrument instrument = new MelodyInstrument(ticksPerBar, program, volume);
    instruments.add(instrument);
    return instrument;
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

  public PolyphonicInstrument drop2chords(int highestMidiPitch, Program program, int volume, int pan) {
    ChordGenerator chordGenerator = new Drop2ChordGenerator(highestMidiPitch);
    PolyphonicInstrument instrument = new PolyphonicInstrument(beatsPerBar, ticksPerBar, nextMidiChannel(), chordGenerator, program, volume, pan);
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

  public String getStyle() {
    return style;
  }
  
  public int getBpm() {
    return tempo.getBpm();
  }
  @Override
  public String toString() {
    return style.concat(" ").concat(Integer.toString(tempo.getBpm())).concat(" bpm");
  }
}
