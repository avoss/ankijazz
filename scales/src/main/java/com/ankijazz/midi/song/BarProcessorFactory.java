package com.ankijazz.midi.song;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.ankijazz.midi.CompositePart;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.theory.Scale;

public class BarProcessorFactory implements EventProcessor {
  
  private final List<BiConsumer<CompositePart, Bar>> consumers = new ArrayList<>();
  private final BiFunction<Event, Scale, Part> player;
  private final int denominator;
  
  public BarProcessorFactory(int denominator, BiFunction<Event, Scale, Part> player) {
    this.denominator = denominator;
    this.player = player;
  }

  private class OnEvent implements BiConsumer<CompositePart, Bar> {
    private Event event;

    OnEvent(Event event) {
      this.event = event;
    }

    @Override
    public void accept(CompositePart parts, Bar bar) {
      Scale chord = bar.getChordForBeat(event.getBeat()).getScale();
      Part part = player.apply(event, chord);
      parts.add(part);
      parts.add(Parts.rest(denominator));
    }
    
  }
  
  private class OnEmpty implements BiConsumer<CompositePart, Bar> {
    @Override
    public void accept(CompositePart parts, Bar bar) {
      parts.add(Parts.rest(denominator));
    }
  }
  
  @Override
  public void event(Event event) {
    consumers.add(new OnEvent(event));
  }

  @Override
  public void empty() {
    consumers.add(new OnEmpty());
  }
  
  public BarProcessor create() {
    return new BarProcessor(consumers);
  }

}
