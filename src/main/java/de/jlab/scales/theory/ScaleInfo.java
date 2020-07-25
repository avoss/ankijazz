package de.jlab.scales.theory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleInfo {
  private String name;
  private Accidental accidental;
  private Scale scale;
  private Scale parent;
  
  public boolean isInversion() {
    return !scale.equals(parent);
  }
}
