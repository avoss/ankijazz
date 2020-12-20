package de.jlab.scales.midi.song;

/**
 * maps interval number to chord index. For example, Cmaj has notes C E G. The interval 5 means G, but its index in the chord is 2
 */
public class IntervalToChordIndexMapper {
  
  public int map(int interval) {
    return (interval -1) / 2;
  }

}
