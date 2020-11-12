package de.jlab.scales.rhythm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.fraction.Fraction;

public class EventSequence {
  
  private List<Event> events = new ArrayList<>();
  
  public EventSequence() {
  }

  public EventSequence(Event ... events) {
    this(Arrays.asList(events));
  }
  
  public EventSequence(List<Event> events) {
    this.events.addAll(events);
  }
  
  public Fraction getLength() {
    Fraction result = Fraction.ZERO;
    for (Event event: events) {
      result = result.add(event.getLength());
    }
    return result;
  }

  public EventSequence add(Event event) {
    EventSequence copy = new EventSequence(events);
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

  public Object getNumberOfEvents() {
    return events.size();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((events == null) ? 0 : events.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EventSequence other = (EventSequence) obj;
    if (events == null) {
      if (other.events != null)
        return false;
    } else if (!events.equals(other.events))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return events.toString();
  }

}
