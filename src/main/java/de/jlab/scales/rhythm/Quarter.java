package de.jlab.scales.rhythm;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;

import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.difficulty.WithDifficulty;

@lombok.EqualsAndHashCode
public class Quarter implements WithDifficulty {
  
  private static final double MAX_BEATS = 4;
  private final List<Event> events = new ArrayList<>();
  private final boolean tied;
  private final double difficulty;
  
  public Quarter() {
    this(emptyList());
  }

  public Quarter(Event ... events) {
    this(Arrays.asList(events));
  }
  
  public Quarter(List<Event> events) {
    this(false, events);
  }

  public Quarter(boolean tied, List<Event> events) {
    this.tied = tied;
    events.stream().forEach(this::internalAdd);
    this.difficulty = computeDifficulty();
  }
  
  public Fraction getLength() {
    return events.stream().map(Event::getLength).collect(Collectors.reducing(Fraction.ZERO, (a,b) -> a.add(b)));
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

  public double getDifficulty() {
    return difficulty;
  }
  
  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(1, MAX_BEATS, 250).update(getNumberOfEvents());
    model.booleanTerm(500).update(isTriplet());
    model.booleanTerm(70).update(isTied());
    model.booleanTerm(40).update(isSyncopated());
    Fraction time = Fraction.ZERO;
    for (Event e : events) {
      model.doubleTerm(1, 8, 200).update(difficulty(time));
      model.booleanTerm(10).update(!e.isBeat());
      time = time.add(e.getLength());
    }
    return model.getDifficulty();
  }

  private int difficulty(Fraction time) {
    if (isDividable(time, 4)) {
      return 1;
    }
    if (isDividable(time, 2)) {
      return 2;
    }
    if (isDividable(time, 3)) {
      return 8;
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
  
  public boolean isSyncopated() {
    int numberOfRests = (int) events.stream().filter(e -> !e.isBeat()).count();
    return numberOfRests == 1 && !startsWithBeat();
  }

  public static boolean hasTies(Collection<? extends Quarter> quarters) {
    return quarters.stream().filter(Quarter::isTied).findAny().isPresent();
  }

  /**
   * return events of a quarter note typically
   */
  public static Quarter q(Event ... events) {
    return new Quarter(events);
  }

}
