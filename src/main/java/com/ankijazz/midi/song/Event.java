package com.ankijazz.midi.song;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Event {
  private final int patternIndex;
  private final int patternId;
  private final int velocity;
  private final int beat;
  private int noteLength;
  
  public String getId() {
    return Integer.toString(patternId).concat(":").concat(Integer.toString(patternIndex));
  }
  
}