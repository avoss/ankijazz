package com.ankijazz.midi.song;

public interface EventProcessor {
  void event(Event event);
  void empty();
}
