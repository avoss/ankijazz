package de.jlab.scales.random;

public class Limiter implements Sequence<Boolean> {

  private Sequence<Boolean> master;
  private PatternBoolean limiter;
  private int limitingCounter;
  
  public Limiter(String pattern, Sequence<Boolean> master) {
    this.limiter = new PatternBoolean(pattern);
    this.master = master;
  }

  @Override
  public Boolean next() {
    boolean result = master.next();
    if (limitingCounter > 0) {
      limitingCounter -= 1;
      if (!limiter.next())
        return false;
    }
    if (result) {
      limiter.reset();
      limitingCounter = limiter.size();
    }
    return result;
  }

  @Override
  public void reset() {
    master.reset();
    limiter.reset();
    limitingCounter = 0;
  }
  
  @Override
  public int size() {
    return master.size();
  }

}
