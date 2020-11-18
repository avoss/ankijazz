package de.jlab.scales.rhythm;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.math3.fraction.Fraction;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

public class EventSequences {

  private final Fraction ticksPerQuarter;
  private SetMultimap<EventSequenceCategory,EventSequence> result = LinkedHashMultimap.create();
  private Collection<Event> events;
  private final Predicate<EventSequence> filter;

  public EventSequences() {
    this(4, asList(Event.values()), new SaherGaltEventSequenceFilter());
  }
  
  public EventSequences(int ticksPerQuarter, Collection<Event> events, Predicate<EventSequence> filter) {
    this.filter = filter;
    this.ticksPerQuarter = new Fraction(ticksPerQuarter, 1);
    this.events = events;
    recurse(new EventSequence());
  }


  private void recurse(EventSequence current) {
    if (current.getLength().equals(ticksPerQuarter)) {
      if (filter.test(current)) {
        result.put(current.getCategory(), current);
      }
      return;
    }
    if (current.getLength().compareTo(ticksPerQuarter) > 0) {
      return;
    }
    for (Event event : events) {
      recurse(current.add(event));
    }
  }
  
  public Map<EventSequenceCategory, Collection<EventSequence>> getEventSequenceMap() {
    return result.asMap();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Map<EventSequenceCategory, Collection<EventSequence>> map = result.asMap();
    for (EventSequenceCategory category : map.keySet()) {
      sb.append("Category: ").append(category.getBeatPositions()).append("\n");
      for (EventSequence sequence : map.get(category)) {
        sb.append(String.format("  %3d %s\n", sequence.getDifficulty(), sequence.getEvents()));
      }
    }
    return sb.toString();
  }
  
}
