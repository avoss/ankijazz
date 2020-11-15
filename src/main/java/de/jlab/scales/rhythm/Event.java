package de.jlab.scales.rhythm;

import java.util.List;
import java.util.Optional;

import org.apache.commons.math3.fraction.Fraction;

/**
 * b1 .. b4 = beats of length 1 .. 4 ticks (where tick is usually 1/16th note)
 * r1 .. r4 = rest of length 1 .. 4
 * bt and rt = beat and rest of length one 8th triplet
 */
public enum Event {
  
  b1(1,1,true), 
  b2(2,1,true),
  b3(3,1,true),
  b4(4,1,true),
  r1(1,1,false),
  r2(2,1,false),
  r3(3,1,false),
  r4(4,1,false),
  r6(6,1,false),
  r8(8,1,false),
  bt(4,3,true) {
    @Override
    public boolean isTriplet() {
      return true;
    }
  },  
  rt(4,3,false) {
    @Override
    public boolean isTriplet() {
      return true;
    }
  }; 

  private Fraction length;
  private boolean beat;

  Event(int numerator, int denominator, boolean beat) {
    this.beat = beat;
    this.length = new Fraction(numerator, denominator);
  }
  
  public Fraction getLength() {
    return length;
  }
  
  public boolean isCombinableWith(Event event) {
    if (isBeat() || event.isBeat()) {
      return false;
    }
    return combinedRest(event).isPresent();
  }

  public Event combineWith(Event event) {
    return combinedRest(event).get();
  }
  
  private Optional<Event> combinedRest(Event event) {
    Fraction targetLength = this.getLength().add(event.getLength());
    return List.of(Event.values()).stream().filter(e -> !e.isBeat() && e.getLength().equals(targetLength)).findAny();
  }

  
  public boolean isBeat() {
    return beat;
  }

  public boolean isTriplet() {
    return false;
  }
}
