package de.jlab.scales.midi.song;

import static de.jlab.scales.Utils.loopIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Sequential;
import de.jlab.scales.theory.Scale;
import lombok.Getter;

@Getter
public abstract class AbstractInstrument<T extends AbstractInstrument<T>> implements Instrument {

  private final int denominator;

  private List<BarProcessor> barProcessors = new ArrayList<>();
  private Iterator<BarProcessor> barProcessorIterator;
  private int patternId;

  protected AbstractInstrument(int denominator) {
    this.denominator = denominator;
  }

  protected List<String> parse(String pattern) {
    BarProcessorFactory factory = new BarProcessorFactory(denominator, getPlayer());
    List<String> eventIds = EventParser.parseEvents(factory, pattern, patternId);
    barProcessors.add(factory.create());
    barProcessorIterator = loopIterator(barProcessors);
    patternId++;
    return eventIds;
  }

  @Override
  public Part play(Song song) {
    Sequential container = new Sequential();
    for (Bar bar : song.getBars()) {
      barProcessorIterator.next().accept(container, bar);
    }
    return container;
  }

  protected abstract BiFunction<Event, Scale, Part> getPlayer();
}
