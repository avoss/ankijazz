package de.jlab.scales.midi.song;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Event {
  final int patternId;
  final int patternLength;
  final int patternIndex;
  final int velocity;
  final int noteLength;
}