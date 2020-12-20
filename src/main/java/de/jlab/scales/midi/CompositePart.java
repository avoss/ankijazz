package de.jlab.scales.midi;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class CompositePart extends AbstractPart {
  protected List<Part> parts = new ArrayList<Part>();
  
  protected CompositePart(Part... parts) {
    for (Part p : parts)
      this.parts.add(p);
  }
  
  protected CompositePart(Part part, Part... parts) {
    this.parts.add(part);
    for (Part p : parts)
      this.parts.add(p);
  }

  public void add(Part ...parts) {
    for (Part part: parts) {
      this.parts.add(part);
    }
  }


}
