package com.ankijazz.midi.song;

import static com.ankijazz.Utils.getLast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.ankijazz.midi.MidiUtils;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.Stacker;

public class Drop2ChordGenerator implements ChordGenerator {

  private static final int NUMBER_OF_NOTES_LIMIT = 4;
  private final int highestMidiPitch;
  private final NoteToMidiMapper mapper;

  public Drop2ChordGenerator(int highestMidiPitch) {
    this.highestMidiPitch = highestMidiPitch;
    this.mapper = NoteToMidiMapper.range(0, highestMidiPitch);
  }

  @Override
  public int[] midiChord(Scale chord) {
    List<Note> notes = new Stacker(chord).getStackedThirds();
    notes = limitNumberOfNotes(chord.getRoot(), notes);
    notes = findBestInversion(notes);
    int dropIndex = dropIndex(notes);
    return drop2MidiPitches(dropIndex, notes);
  }

  private int[] drop2MidiPitches(int dropIndex, List<Note> inversion) {
    int[] result = new int[inversion.size()];
    int resultIndex = 0;
    mapper.resetToHighest();
    ListIterator<Note> iterator = inversion.listIterator(inversion.size());
    Note dropNote = inversion.get(dropIndex);
    while (iterator.hasPrevious()) {
      Note note = iterator.previous();
      if (note == dropNote) {
        continue;
      }
      result[resultIndex++] = nextLower(note);
    }
    result[resultIndex++] = nextLower(dropNote);
    return result;
  }

  private int nextLower(Note note) {
    int lower = mapper.nextLower(note);
    if (lower < highestMidiPitch - 16) {
      lower += 12;
    }
    return lower;
  }

  List<Note> findBestInversion(List<Note> notes) {
    List<Note> bestInversion = notes;
    int bestDistance = distance(notes);
    for (int i = 1; i < notes.size(); i++) {
      List<Note> inversion = new ArrayList<>(notes);
      Collections.rotate(inversion, i);
      int distance = distance(inversion);
      if (distance < bestDistance) {
        bestDistance = distance;
        bestInversion = inversion;
      }
    }
    return bestInversion;
  }

  List<Note> limitNumberOfNotes(Note root, List<Note> notes) {
    if (notes.size() > NUMBER_OF_NOTES_LIMIT) {
      notes.remove(root);
    }
    if (notes.size() > NUMBER_OF_NOTES_LIMIT) {
      notes.remove(root.five());
    }
    return notes;
  }

  private int distance(List<Note> notes) {
    return highestMidiPitch - MidiUtils.noteToMidiPitchBelowOrSame(highestMidiPitch, getLast(notes));
  }
  

  int dropIndex(List<Note> notes) {
    switch(notes.size()) {
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
