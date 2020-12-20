package de.jlab.scales.midi.song;

import static java.util.stream.Collectors.toList;

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

  public EventParser(EventProcessor processor, int patternId) {
    this.processor = processor;
    this.patternId = patternId;
  }

  public static List<String> parseEvents(EventProcessor processor, String pattern, int patternId) {
    return new EventParser(processor, patternId).internalParse(pattern);
  }

  private List<String> internalParse(String pattern) {
    values = Patterns.parse(pattern);
    int patternLength = values.length;
    Interpolator interpolator = Utils.interpolator(0, 127);
    int patternIndex = 0;
    List<Event> events = new ArrayList<>();
    while (patternIndex < patternLength) {
      if (values[patternIndex] > 0) {
        int noteLength = noteLength(patternIndex);
        Event event = Event.builder()
            .patternLength(patternLength)
            .patternIndex(patternIndex)
            .eventId(Integer.toString(patternId).concat(":").concat(Integer.toString(patternIndex)))
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
    return events.stream().map(Event::getEventId).collect(toList());
  }

  private int noteLength(int patternIndex) {
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
