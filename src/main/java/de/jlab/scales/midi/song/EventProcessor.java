package de.jlab.scales.midi.song;

public interface EventProcessor {
  void event(Event event);
  void empty();
}
