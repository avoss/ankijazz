package de.jlab.scales.theory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
  private final List<Scale> superScales = new ArrayList<>();
  private final List<Scale> subScales = new ArrayList<>();

  public boolean isInversion() {
    return !scale.equals(parent);
  }
}
