package de.jlab.scales.midi.song;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class MonophonicInstrument extends TonalInstrument<MonophonicInstrument> {

  private final Map<String, Integer> chordIndexMap = new HashMap<>();
  private final NoteToMidiMapper noteToMidiMapper;
  private final int midiChannel;
  private int denominator;
  
  // TODO use parameter object
  public MonophonicInstrument(int beatsPerBar, int ticksPerBar, int midiChannel, NoteToMidiMapper noteToMidiMapper, Program program, int volume, int pan) {
    super(beatsPerBar, ticksPerBar, midiChannel, program, volume, pan);
    this.denominator = ticksPerBar;
    this.midiChannel = midiChannel;
    this.noteToMidiMapper = noteToMidiMapper;
  }

  /**
   * for every note in pattern, we need to specify which note from the chord to pick (1 = root, 3 = 3rd, 5 = fifth etc)
   */
  public MonophonicInstrument bar(String pattern, int ... intervals) {
    List<String> eventIds = super.parse(pattern);
    if (eventIds.size() != intervals.length) {
      throw new IllegalArgumentException("number of notes in pattern must match the numbers of intervals");
    }
    int index = 0;
    for (String eventId : eventIds) {
      chordIndexMap.put(eventId, intervals[index++]);
    }
    return this;
  }

  @Override
  protected BiFunction<Event, Scale, Part> getPlayer() {
    IntervalToChordIndexMapper mapper = new IntervalToChordIndexMapper();
    return (event, scale) -> {
      int interval = chordIndexMap.get(event.getId());
      Note note = mapper.map(scale, interval);
      int midiPitch = noteToMidiMapper.nextClosest(note);
      return Parts.note(midiChannel, midiPitch, event.getVelocity(), event.getNoteLength(), denominator);
    };
  }

}
