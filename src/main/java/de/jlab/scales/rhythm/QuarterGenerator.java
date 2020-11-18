package de.jlab.scales.rhythm;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.math3.fraction.Fraction;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

public class QuarterGenerator {

  private final Fraction ticksPerQuarter;
  private SetMultimap<QuarterCategory,Quarter> result = LinkedHashMultimap.create();
  private Collection<Event> events;
  private final Predicate<Quarter> filter;

  public QuarterGenerator() {
    this(4, asList(Event.values()), new SaherGaltEventFilter());
  }
  
  public QuarterGenerator(int ticksPerQuarter, Collection<Event> events, Predicate<Quarter> filter) {
    this.filter = filter;
    this.ticksPerQuarter = new Fraction(ticksPerQuarter, 1);
    this.events = events;
    recurse(new Quarter());
  }


  private void recurse(Quarter current) {
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
  
  public Map<QuarterCategory, Collection<Quarter>> getQuarterMap() {
    return result.asMap();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Map<QuarterCategory, Collection<Quarter>> map = result.asMap();
    for (QuarterCategory category : map.keySet()) {
      sb.append("Category: ").append(category.getBeatPositions()).append("\n");
      for (Quarter quarter : map.get(category)) {
        sb.append(String.format("  %3d %s\n", quarter.getDifficulty(), quarter.getEvents()));
      }
    }
    return sb.toString();
  }
  
}
