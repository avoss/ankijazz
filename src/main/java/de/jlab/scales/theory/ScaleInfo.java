package de.jlab.scales.theory;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleInfo {
  private String name;
  private String typeName;
  private Accidental accidental;
  private Scale scale;
  private Scale parent;
  private final Set<Scale> superScales = new HashSet<>();
  private final Set<Scale> subScales = new HashSet<>();

  public boolean isInversion() {
    return !scale.equals(parent);
  }
}
