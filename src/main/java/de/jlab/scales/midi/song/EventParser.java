package de.jlab.scales.midi.song;

import java.util.ArrayList;
import java.util.List;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.random.Patterns;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EventParser {

  private double[] values;
  private final EventProcessor processor;
  private final int patternId;

  @RequiredArgsConstructor
  @Getter
  public static class PatternMetadata {
    private final int patternLength;
    private final List<Event> events;
  }
  
  public EventParser(EventProcessor processor, int patternId) {
    this.processor = processor;
    this.patternId = patternId;
  }

  public static PatternMetadata parseEvents(EventProcessor processor, String pattern, int patternId) {
    return new EventParser(processor, patternId).internalParse(pattern);
  }

  private PatternMetadata internalParse(String pattern) {
    values = Patterns.parse(pattern);
    int patternLength = values.length;
    Interpolator interpolator = Utils.interpolator(0, 127);
    int patternIndex = 0;
    List<Event> events = new ArrayList<>();
    while (patternIndex < patternLength) {
      if (values[patternIndex] > 0) {
        int noteLength = notLength(patternIndex);
        Event event = Event.builder()
            .patternLength(patternLength)
            .patternIndex(patternIndex)
            .patternId(patternId)
            .velocity(interpolator.apply(values[patternIndex]))
            .noteLength(noteLength)
            .build();
        processor.event(event);
        events.add(event);
      } else {
        processor.empty();
      }
      patternIndex++;
    }
    return new PatternMetadata(patternLength, events);
  }

  private int notLength(int patternIndex) {
    int length = 1;
    for (int i = patternIndex + 1; i < values.length; i++) {
      if (values[i] == -1.0) {
        length += 1;
      } else {
        break;
      }
    }
    return length;
  }

}
