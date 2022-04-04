package com.ankijazz.midi.song;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ankijazz.Utils;
import com.ankijazz.Utils.Interpolator;

public class RhythmParser {
  
  private final int ticksPerBeat;

  private int patternId;
  private Optional<Event> previousEvent = Optional.empty();
  private int additionalLengthOfLastEvent = 0;

  public RhythmParser(int ticksPerBeat) {
    this.ticksPerBeat = ticksPerBeat;
  }


  class Handler implements PatternParser.Handler {
    private final Interpolator interpolator = Utils.interpolator(0, 127);
    private final List<Event> events = new ArrayList<>();
    private final List<String> eventIds = new ArrayList<>();
    private final int ticksPerBeat;
    private final EventProcessor processor;

    private int patternIndex = 0;
    private int useNextChordPatternIndex;

    Handler(int ticksPerBeat, EventProcessor processor) {
      this.ticksPerBeat = ticksPerBeat;
      this.processor = processor;
    }
    
    @Override
    public void onValue(double value) {
      int beat = patternIndex / ticksPerBeat
          + (useNextChordPatternIndex > patternIndex ? 1 : 0);
      Event event = Event.builder()
          .patternId(patternId)
          .patternIndex(patternIndex)
          .velocity(interpolator.apply(value))
          .noteLength(1)
          .beat(beat)
          .build();
      processor.event(event);
      events.add(event);
      eventIds.add(event.getId());
      previousEvent = Optional.of(event);
      patternIndex++;
    }

    @Override
    public void onDash() {
      if (previousEvent.isPresent()) {
        Event event = previousEvent.get();
        event.setNoteLength(event.getNoteLength() + 1);
      } else {
        additionalLengthOfLastEvent ++;
      }
      onEmpty();
    }

    @Override
    public void onEmpty() {
      processor.empty();
      patternIndex++;
    }

    @Override
    public void onNext() {
      useNextChordPatternIndex = patternIndex + ticksPerBeat;
    }
    
    public List<String> getEventIds() {
      return eventIds;
    }
  }
  
  public List<String> parse(String pattern, EventProcessor processor) {
    Handler handler = new Handler(ticksPerBeat, processor);
    PatternParser.parse(pattern, handler);
    List<String> eventIds = handler.getEventIds();
    patternId++;
    return eventIds;
  }

  public void close() {
    if (previousEvent.isPresent()) {
      Event event = previousEvent.get();
      event.setNoteLength(event.getNoteLength() + additionalLengthOfLastEvent);
      previousEvent = Optional.empty();
    }
  }
  
}
