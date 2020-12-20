package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.MidiUtils.midiPitchToNote;

import de.jlab.scales.theory.Note;

public class NoteToMidiMapper {

  private final int lowestMidiPitch;
  private final int highestMidiPitch;
  private final int centerMidiPitch;
  
  private int previousMidiPitch;
  private Note previousNote;

  public static NoteToMidiMapper octave(int lowestMidiPitch) {
    return new NoteToMidiMapper(lowestMidiPitch, lowestMidiPitch + 12);
  }
  
  public static NoteToMidiMapper range(int lowestMidiPitch, int highestMidiPitch) {
    return new NoteToMidiMapper(lowestMidiPitch, highestMidiPitch);
  }

  private NoteToMidiMapper(int lowestMidiPitch, int highestMidiPitch) {
    this.lowestMidiPitch = lowestMidiPitch;
    this.highestMidiPitch = highestMidiPitch;
    this.centerMidiPitch = (lowestMidiPitch + highestMidiPitch) / 2;
    this.previousMidiPitch = centerMidiPitch;
    this.previousNote = midiPitchToNote(previousMidiPitch);
    if (highestMidiPitch - lowestMidiPitch < 12) {
      throw new IllegalArgumentException("Midi range must at least include one octave");
    }
  }

  public int nextClosest(Note note) {
    int nextHigherMidiPitch = tryHigher(note);
    int nextLowerMidiPitch = tryLower(note);
    int higherDistance = Math.abs(previousMidiPitch - nextHigherMidiPitch);
    int lowerDistance = Math.abs(previousMidiPitch - nextLowerMidiPitch);
    if (higherDistance == lowerDistance) {
      higherDistance = Math.abs(centerMidiPitch - nextHigherMidiPitch);
      lowerDistance = Math.abs(centerMidiPitch - nextLowerMidiPitch);
    }
    if (higherDistance < lowerDistance) {
      return nextHigher(note);
    }
    return nextLower(note);
  }
  
  public int nextHigher(Note note) {
    int pitch = tryHigher(note);
    if (pitch <= highestMidiPitch) {
      previousMidiPitch = pitch;
      previousNote = note;
      return pitch;
    }
    return nextLower(note);
  }

  public int nextLower(Note note) {
    int pitch = tryLower(note);
    if (pitch >= lowestMidiPitch) {
      previousMidiPitch = pitch;
      previousNote = note;
      return pitch;
    }
    return nextHigher(note);
  }

  private int tryHigher(Note note) {
    int diff = note.ordinal() - previousNote.ordinal();
    if (diff < 0) {
      diff += Note.values().length;
    }
    return previousMidiPitch + diff;
  }

  private int tryLower(Note note) {
    int diff = note.ordinal() - previousNote.ordinal();
    if (diff > 0) {
      diff -= Note.values().length;
    }
    return previousMidiPitch + diff;
  }
  
  public NoteToMidiMapper resetToLowest() {
    return resetTo(lowestMidiPitch);
  }

  public NoteToMidiMapper resetToCenter() {
    return resetTo(centerMidiPitch);
  }
  
  public NoteToMidiMapper resetToHighest() {
    return resetTo(highestMidiPitch);
  }

  private NoteToMidiMapper resetTo(int midiPitch) {
    previousMidiPitch = midiPitch;
    previousNote = midiPitchToNote(midiPitch);
    return this;
  }
  
}
