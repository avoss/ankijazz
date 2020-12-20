package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class AbstractPart implements Part {

  public Part repeat(int times) {
    return new Repeat(this, times);
  }

  public Part append(Part... parts) {
    return new Sequential(this, parts);
  }
  
  public Part merge(Part ...parts) {
    return new Parallel(this, parts);
  }

}
