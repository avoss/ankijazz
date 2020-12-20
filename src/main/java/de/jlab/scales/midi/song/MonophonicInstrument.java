package de.jlab.scales.midi.song;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;
import de.jlab.scales.midi.song.EventParser.PatternMetadata;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class MonophonicInstrument extends TonalInstrument<MonophonicInstrument> {

  private final Map<Event, Integer> chordIndexMap = new HashMap<>();
  private final NoteToMidiMapper noteToMidiMapper;
  private final int midiChannel;
  private int denominator;
  
  // TODO use parameter object
  public MonophonicInstrument(int denominator, int midiChannel, NoteToMidiMapper noteToMidiMapper, Program program, int volume, int pan) {
    super(denominator, midiChannel, program, volume, pan);
    this.denominator = denominator;
    this.midiChannel = midiChannel;
    this.noteToMidiMapper = noteToMidiMapper;
  }

  /**
   * for every note in pattern, we need to specify which note from the chord to pick (1 = root, 3 = 3rd, 5 = fifth etc)
   */
  public MonophonicInstrument bar(String pattern, int ... intervals) {
    PatternMetadata metadata = super.parse(pattern);
    if (metadata.getEvents().size() != intervals.length) {
      throw new IllegalArgumentException("number of notes in pattern must match the numbers of intervals");
    }
    int index = 0;
    IntervalToChordIndexMapper indexMapper = new IntervalToChordIndexMapper();
    for (Event event : metadata.getEvents()) {
      if (chordIndexMap.containsKey(event)) {
        throw new IllegalStateException("should not happen - was equals/hashcode modified? Kind of useless field patternId removed?");
      }
      chordIndexMap.put(event, indexMapper.map(intervals[index++]));
    }
    return this;
  }

  @Override
  protected BiFunction<Event, Scale, Part> getPlayer() {
    return (event, scale) -> {
      int index = chordIndexMap.get(event);
      Note note = scale.getNote(index);
      int midiPitch = noteToMidiMapper.nextClosest(note);
      return Parts.note(midiChannel, midiPitch, event.getVelocity(), event.getNoteLength(), denominator);
    };
  }

}
