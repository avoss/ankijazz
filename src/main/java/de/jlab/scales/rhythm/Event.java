package de.jlab.scales.rhythm;

import org.apache.commons.math3.fraction.Fraction;

/**
 * b1 .. b4 = beats of length 1 .. 4 ticks (where tick is usually 1/16th note)
 * r1 .. r4 = rest of length 1 .. 4
 * bt and rt = beat and rest of length one 8th triplet
 */
public enum Event {
  
  b1(1,1,4,true), 
  b2(2,1,2,true),
  b3(3,1,3,true),
  b4(4,1,1,true),
  r1(1,1,5,false) {
    @Override
    public boolean isCombinableWith(Event event) {
      switch(event) {
      case r1:
      case r2:
      case r3:
        return true;
      default: 
        return false;
      }
    }

    @Override
    public Event combineWith(Event event) {
      switch(event) {
      case r1:
        return r2;
      case r2:
        return r3;
      case r3:
        return r4;
      default: 
        throw new IllegalArgumentException("cannot combine " + event + " with " + this);
      }
    }
    
  },  
  r2(2,1,3,false) {
    @Override
    public boolean isCombinableWith(Event event) {
      switch(event) {
      case r1:
      case r2:
        return true;
      default: 
        return false;
      }
    }

    @Override
    public Event combineWith(Event event) {
      switch(event) {
      case r1:
        return r3;
      case r2:
        return r4;
      default: 
        throw new IllegalArgumentException("cannot combine " + event + " with " + this);
      }
    }
  },
  r3(3,1,4,false) {
    @Override
    public boolean isCombinableWith(Event event) {
      switch(event) {
      case r1:
        return true;
      default: 
        return false;
      }
    }

    @Override
    public Event combineWith(Event event) {
      switch(event) {
      case r1:
        return r4;
      default: 
        throw new IllegalArgumentException("cannot combine " + event + " with " + this);
      }
    }
  },
  r4(4,1,0,false);
//  bt(4,3,7,true),  
//  rt(4,3,12,false); 

  private Fraction length;
  private int difficulty;
  private boolean beat;

  Event(int numerator, int denominator, int difficulty, boolean beat) {
    this.difficulty = difficulty;
    this.beat = beat;
    this.length = new Fraction(numerator, denominator);
  }
  
  public Fraction getLength() {
    return length;
  }
  
  public int getDifficulty() {
    return difficulty;
  }

  public boolean isCombinableWith(Event event) {
    return false;
  }

  public Event combineWith(Event event) {
    throw new UnsupportedOperationException();
  }
  
  public boolean isBeat() {
    return beat;
  }
}
