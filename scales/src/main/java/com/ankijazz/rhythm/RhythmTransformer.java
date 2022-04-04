package com.ankijazz.rhythm;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.fraction.Fraction;

import com.ankijazz.rhythm.RhythmTransformer.Tick.TickBuilder;
import com.google.common.collect.Lists;

public class RhythmTransformer {
  
  @lombok.Builder
  @lombok.Data
  static class Tick {
    private boolean beat;
    private boolean tied;
  }
  
  public List<Quarter> transpose(List<Quarter> quarters, int distance) {
    int ticksPerQuarter = ticksPerQuarter(quarters);
    List<Tick> ticks = disassemble(quarters);
    Collections.rotate(ticks, distance);
    removeExtraBeats(ticks);
    return assemble(ticks, ticksPerQuarter);
  }

  private void removeExtraBeats(List<Tick> ticks) {
    Tick last = ticks.get(ticks.size()-1);
    if (!last.isTied()) {
      return;
    }
    last.setTied(false);
    
    Iterator<Tick> iter = ticks.iterator();
    Tick prev = iter.next();
    while (iter.hasNext()) {
      Tick next = iter.next();
      prev.setBeat(false);
      if (!prev.isTied()) {
        if (!next.isBeat()) {
          prev.setTied(true);
        }
        return;
      }
      prev = next;
    }
  }

  List<Quarter> assemble(List<Tick> ticks, int ticksPerQuarter) {
    List<List<Tick>> chunks = Lists.partition(ticks, ticksPerQuarter);
    return chunks.stream().map(this::ticksToQuarter).collect(toList());
  }

  Quarter ticksToQuarter(List<Tick> ticks) {
    List<Event> events = new ArrayList<>();
    int length = 0;
    Tick lastTick = null;
    for (Tick tick : ticks) {
      length += 1;
      if (!tick.isTied()) {
        events.add(Event.find(tick.isBeat(), length));
        length = 0;
      }
      lastTick = tick;
    }
    if (length > 0) {
      events.add(Event.find(lastTick.isBeat(), length));
    }
    return new Quarter(lastTick.isTied(), events);
  }
  
  List<Tick> disassemble(List<Quarter> quarters) {
    return quarters.stream()
        .flatMap(q -> quarterToTicks(q).stream())
        .collect(toList());
  }

  private List<Tick> quarterToTicks(Quarter quarter) {
    List<Tick> result = new ArrayList<>();
    Tick lastTick = null;
    for (Event event: quarter.getEvents()) {
      int length = event.getLength().intValue();
      TickBuilder builder = Tick.builder().beat(event.isBeat());
      for (int i = 0; i < length - 1; i++) {
        result.add(builder.tied(true).build());
      }
      lastTick = builder.tied(false).build();
      result.add(lastTick);
    }
    lastTick.setTied(quarter.isTied());
    return result;
  }

  int ticksPerQuarter(List<Quarter> quarters) {
    int ticksPerQuarter = quarters.get(0).getLength().intValue();
    Fraction expectedLength = new Fraction(ticksPerQuarter);
    if (quarters.stream().map(Quarter::getLength).filter(actualLength -> !actualLength.equals(expectedLength)).findAny().isPresent()) {
      throw new IllegalArgumentException("non-int or mixed length of quarters");
    }
    if (quarters.stream().flatMap(q -> q.getEvents().stream()).filter(e -> !new Fraction(e.getLength().intValue()).equals(e.getLength())).findAny().isPresent()) {
      throw new IllegalArgumentException("non-int event length (triplets not supported)");
    }
    return ticksPerQuarter;
  }

}
