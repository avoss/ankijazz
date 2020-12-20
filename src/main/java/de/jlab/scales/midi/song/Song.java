package de.jlab.scales.midi.song;

import java.util.List;

import lombok.Getter;

@Getter
public class Song {
  private final List<Bar> bars;
  
  public Song(List<Bar> bars) {
    this.bars = bars; 
  }
  
  public static Song of(Bar ...bars) {
    return new Song(List.of(bars));
  }
}
