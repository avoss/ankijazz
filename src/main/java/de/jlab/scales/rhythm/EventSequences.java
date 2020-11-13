package de.jlab.scales.rhythm;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.jlab.scales.rhythm.Event.*;

import org.apache.commons.math3.fraction.Fraction;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

public class EventSequences {

  private final Set<EventSequence> invalid = new HashSet<>(Arrays.asList(
      new EventSequence(rt,rt,rt)
      , new EventSequence(rt,bt,rt)
      , new EventSequence(bt,rt,bt)
      , new EventSequence(bt,rt,rt)
//      , new EventSequence(rt,rt,bt)
      , new EventSequence(r4)
      ));
  private final Fraction ticksPerQuarter;
  private SetMultimap<EventSequenceCategory,EventSequence> result = LinkedHashMultimap.create();
  private Collection<Event> events;

  public EventSequences() {
    this(4, asList(Event.values()));
  }
  
  public EventSequences(int ticksPerQuarter, Collection<Event> events) {
    this.ticksPerQuarter = new Fraction(ticksPerQuarter, 1);
    this.events = events;
    recurse(new EventSequence());
  }


  private void recurse(EventSequence current) {
    if (current.getLength().equals(ticksPerQuarter)) {
      if (!invalid.contains(current)) {
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
  
  public Map<EventSequenceCategory, Collection<EventSequence>> getResult() {
    return result.asMap();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Map<EventSequenceCategory, Collection<EventSequence>> map = result.asMap();
    for (EventSequenceCategory category : map.keySet()) {
      sb.append("Category: ").append(category.getBeatPositions()).append("\n");
      for (EventSequence sequence : map.get(category)) {
        sb.append("  ").append(sequence.getEvents()).append("\n");
      }
    }
    return sb.toString();
  }
  
}
