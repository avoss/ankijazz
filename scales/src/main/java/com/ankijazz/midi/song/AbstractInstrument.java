package com.ankijazz.midi.song;

import static com.ankijazz.Utils.loopIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import com.ankijazz.midi.Part;
import com.ankijazz.midi.Sequential;
import com.ankijazz.theory.Scale;

import lombok.Getter;

@Getter
public abstract class AbstractInstrument<T extends AbstractInstrument<T>> implements Instrument {

  private final int ticksPerBar;
  private final int ticksPerBeat;
  private final List<BarProcessor> barProcessors = new ArrayList<>();
  private final RhythmParser ryhthmParser;


  protected AbstractInstrument(int beatsPerBar, int ticksPerBar) {
    this.ticksPerBar = ticksPerBar;
    this.ticksPerBeat = ticksPerBar / beatsPerBar;
    this.ryhthmParser = new RhythmParser(ticksPerBeat);
  }

  protected List<String> parse(String pattern) {
    BarProcessorFactory factory = new BarProcessorFactory(ticksPerBar, getPlayer());
    List<String> eventIds = ryhthmParser.parse(pattern, factory);
    barProcessors.add(factory.create());
    return eventIds;
  }

  @Override
  public Part play(Song song) {
    ryhthmParser.close();

    Iterator<BarProcessor> iterator = loopIterator(barProcessors);
    Sequential container = new Sequential();
    for (Bar bar : song.getBars()) {
      iterator.next().accept(container, bar);
    }
    return container;
  }

  protected abstract BiFunction<Event, Scale, Part> getPlayer();
}
