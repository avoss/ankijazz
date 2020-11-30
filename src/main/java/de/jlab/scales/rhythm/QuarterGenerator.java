package de.jlab.scales.rhythm;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.math3.fraction.Fraction;

public class QuarterGenerator {

  private final Fraction ticksPerQuarter;
  private List<Quarter> quarters = new ArrayList<>();
  private List<Event> events = new ArrayList<>();
  private final Predicate<Quarter> filter;

  public QuarterGenerator() {
    this(4, asList(Event.values()), new SaherGaltEventFilter());
  }
  
  public QuarterGenerator(int ticksPerQuarter, Collection<Event> events, Predicate<Quarter> filter) {
    this.filter = filter;
    this.ticksPerQuarter = new Fraction(ticksPerQuarter, 1);
    this.events.addAll(events);
    recurse(new Quarter());
  }

  private void recurse(Quarter current) {
    if (current.getLength().equals(ticksPerQuarter)) {
      if (filter.test(current)) {
        quarters.add(current);
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
  
  public List<Quarter> getQuarters() {
    return quarters;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Quarter quarter : quarters) {
      sb.append(String.format("  %s\n", quarter.getEvents()));
    }
    return sb.toString();
  }
  
}
