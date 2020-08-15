package de.jlab.scales.theory;
import static de.jlab.scales.theory.Accidental.FLAT;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleInfo {
  private String sharpName;
  private String flatName;
  private String defaultName;
  private String modeName;
  private Accidental accidental;
  private Scale scale;
  private Scale parent;
  private final List<Scale> superScales = new ArrayList<>();
  private final List<Scale> subScales = new ArrayList<>();

  public boolean isInversion() {
    return !scale.equals(parent);
  }

  public String getName(Accidental accidental) {
    return accidental == FLAT ? flatName : sharpName;
  }
}
