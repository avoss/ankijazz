package de.jlab.scales.rhythm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static de.jlab.scales.rhythm.Event.*;

import org.apache.commons.math3.fraction.Fraction;

public class Quarters {

  private final Set<EventSequence> invalid = new HashSet<>(Arrays.asList(
      new EventSequence(rt,rt,rt),
      new EventSequence(rt,bt,rt),
      new EventSequence(bt,rt,bt),
      new EventSequence(bt,rt,rt),
      new EventSequence(rt,rt,bt)
      //, new EventSequence(r4)
      ));
  private final Fraction ticksPerQuarter;
  private Set<EventSequence> result = new LinkedHashSet<>();
  private Collection<Event> events;

  public Quarters(int ticksPerQuarter, Collection<Event> events) {
    this.ticksPerQuarter = new Fraction(ticksPerQuarter, 1);
    this.events = events;
    recurse(new EventSequence());
  }

  private void recurse(EventSequence current) {
    if (current.getLength().equals(ticksPerQuarter)) {
      if (!invalid.contains(current)) {
        result.add(current);
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

  @Override
  public String toString() {
    return result.stream().map(r -> r.toString()).collect(Collectors.joining("\n"));
  }

  
}
