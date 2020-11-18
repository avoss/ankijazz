package de.jlab.scales.rhythm;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.fraction.Fraction;

@lombok.EqualsAndHashCode
public class Quarter {
  
  private List<Event> events = new ArrayList<>();
  private boolean tied;
  
  public Quarter() {
  }

  public Quarter(Event ... events) {
    this(Arrays.asList(events));
  }
  
  public Quarter(List<Event> events) {
    this(false, events);
  }

  private Quarter(boolean tied, List<Event> events) {
    this.tied = tied;
    // TODO use internalAdd instead
    this.events.addAll(events);
  }
  
  public Fraction getLength() {
    Fraction result = Fraction.ZERO;
    for (Event event: events) {
      result = result.add(event.getLength());
    }
    return result;
  }

  public Quarter add(Event event) {
    Quarter copy = new Quarter(events);
    copy.internalAdd(event);
    return copy;
  }
  
  private void internalAdd(Event event) {
    if (!events.isEmpty()) {
      int lastIndex = events.size() - 1;
      Event lastEvent = events.get(lastIndex);
      if (lastEvent.isCombinableWith(event)) {
        events.remove(lastEvent);
        events.add(lastEvent.combineWith(event));
        return;
      }
    }
    events.add(event);
  }

  public int getNumberOfEvents() {
    return events.size();
  }
  
  public List<Event> getEvents() {
    return events;
  }

  @Override
  public String toString() {
    return events.stream().map(Event::name).collect(joining()).concat(isTied() ? " ~" : "");
  }

  public QuarterCategory getCategory() {
    Fraction time = Fraction.ZERO;
    List<Fraction> beatPositions = new ArrayList<>();
    for (Event e : events) {
      if (e.isBeat()) {
        beatPositions.add(time);
      }
      time = time.add(e.getLength());
    }
    return new QuarterCategory(beatPositions);
  }

  public int getDifficulty() {
    Fraction time = Fraction.ZERO;
    int difficulty = 0;
    for (Event e : events) {
      difficulty += difficulty(time);
      difficulty += e.isBeat() ? 0 : 1;  // syncopation is more difficult than a beat
      time = time.add(e.getLength());
    }
    return difficulty;
  }

  int difficulty(Fraction time) {
    if (isDividable(time, 4)) {
      return 1;
    }
    if (isDividable(time, 2)) {
      return 2;
    }
    return 3;
  }

  private boolean isDividable(Fraction time, int denominator) {
    return time.divide(denominator).getDenominator() == 1;
  }

  public boolean startsWithBeat() {
    if (events.isEmpty()) {
      return false;
    }
    return events.get(0).isBeat();
  }

  public boolean endsWithBeat() {
    if (events.isEmpty()) {
      return false;
    }
    return events.get(events.size() - 1).isBeat();
  }

  public boolean isTriplet() {
    return !events.isEmpty() && events.get(0).isTriplet();
  }
  
  public boolean isTied() {
    return tied;
  }
  
  public Quarter tie() {
    return new Quarter(true, events);
  }

  public static boolean hasTies(Collection<? extends Quarter> sequences) {
    return sequences.stream().filter(Quarter::isTied).findAny().isPresent();
  }
  /**
   * return events of a quarter note typically
   */
  public static Quarter q(Event ... events) {
    return new Quarter(events);
  }

}
