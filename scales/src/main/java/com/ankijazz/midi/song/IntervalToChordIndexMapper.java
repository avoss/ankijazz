package com.ankijazz.midi.song;

import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.Scales;

/**
 * maps interval number to chord index. For example, Cmaj has notes C E G. The interval 5 means G, but its index in the chord is 2
 * Cm11 chord contains C E F G Bb, still 5 is G 
 */
public class IntervalToChordIndexMapper {
  
  public int map(int interval) {
    return (interval -1) / 2;
  }

  public Note map(Scale scale, int interval) {
    int semitones = Scales.CMajor.getNote(interval - 1).ordinal();
    Note root = scale.getRoot();
    Note perfect = root.transpose(semitones);
    if (scale.contains(perfect)) {
      return perfect;
    }
    Note augmented = perfect.transpose(1);
    if (scale.contains(augmented)) {
      return augmented;
    }
    Note diminished = perfect.transpose(-1);
    if (scale.contains(diminished)) {
      return diminished;
    }
    // not found, play safe root
    return root;
  }

}
