package de.jlab.scales.midi.song;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.midi.CompositePart;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.theory.Scale;

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
      Interpolator interpolator = Utils.interpolator(0, event.getPatternLength(), 0, bar.getChords().size());
      // TODO use "center of gravity" of chord instead of start time, e.g. (start + length)/2
      // if outside this bar, put into next bar - this can only happen, if next bar starts with dashes --- which extend event from this bar
      int chordIndex = interpolator.apply(event.getPatternIndex());
      Scale chord = bar.getChords().get(chordIndex).getScale();
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
