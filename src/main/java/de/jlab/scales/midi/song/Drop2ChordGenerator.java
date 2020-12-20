package de.jlab.scales.midi.song;

import java.util.ArrayList;
import java.util.List;

import de.jlab.scales.midi.MidiUtils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Drop2ChordGenerator implements ChordToMidiMapper {

  private final int lowestMidiPitch;

  public Drop2ChordGenerator(int lowestMidiPitch) {
    this.lowestMidiPitch = lowestMidiPitch;
  }

  @Override
  public int[] midiChord(Scale chord) {
    int dropIndex = dropIndex(chord);
    Scale inversion = findBestInversion(chord, dropIndex);
    return drop2MidiPitches(dropIndex, inversion);
  }

  private int[] drop2MidiPitches(int dropIndex, Scale inversion) {
    int[] result = new int[inversion.getNumberOfNotes()];
    int resultIndex = 0;
    NoteToMidiMapper mapper = NoteToMidiMapper.range(lowestMidiPitch, 127).resetToLowest();
    Note dropNote = inversion.getNote(dropIndex);
    result[resultIndex++] = mapper.nextHigher(dropNote);
    for (Note note : inversion) {
      if (note == dropNote) {
        continue;
      }
      result[resultIndex++] = mapper.nextHigher(note);
    }
    return result;
  }

  Scale findBestInversion(Scale fullChord, int dropIndex) {
    Scale chord = limitNumberOfNotes(fullChord);
    Scale bestInversion = chord;
    int bestDistance = distance(chord, dropIndex);
    for (Scale inversion : chord.getInversions()) {
      int distance = distance(inversion, dropIndex);
      if (distance < bestDistance) {
        bestDistance = distance;
        bestInversion = inversion;
      }
    }
    return bestInversion;
  }

  private Scale limitNumberOfNotes(Scale chord) {
    if (chord.getNumberOfNotes() <= 4) {
      return chord;
    }
    List<Note> notes = new ArrayList<>(chord.asList());
    notes.remove(0);
    return new Scale(notes.get(0), notes);
  }

  private int distance(Scale chord, int dropIndex) {
    return MidiUtils.noteToMidiPitch(lowestMidiPitch, chord.getNote(dropIndex)) - lowestMidiPitch;
  }
  
  int dropIndex(Scale chord) {
    switch(chord.getNumberOfNotes()) {
    case 0:
    case 1:
    case 2:
      return 0;
    case 3: 
      return 1;
    default:
      return 2;
    }
  }

}
