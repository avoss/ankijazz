package de.jlab.scales.midi;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositePart extends AbstractPart {
  protected List<Part> parts = new ArrayList<Part>();
  
  public void add(Part part) {
    parts.add(part);
  }

  protected CompositePart(Part... parts) {
    for (Part p : parts)
      this.parts.add(p);
  }
  
  protected CompositePart(Part part, Part... parts) {
    this.parts.add(part);
    for (Part p : parts)
      this.parts.add(p);
  }

}
