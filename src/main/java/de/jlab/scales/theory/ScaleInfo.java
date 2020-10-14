package de.jlab.scales.theory;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleInfo {
  /**
   * scale: "Bb Dorian"
   * chord: "Bbm7"
   */
  private String scaleName;
  /**
   * scale: "Dorian" 
   * chord: "m7"
   */
  private String typeName;
  private Scale scale;
  private ScaleInfo parentInfo;
  private KeySignature keySignature;
  private final List<Scale> superScales = new ArrayList<>();
  private final List<Scale> subScales = new ArrayList<>();
  private ScaleType scaleType;

  public boolean isInversion() {
    return !scale.equals(parentInfo.getScale());
  }

  public int getModeIndex() {
    return parentInfo.getScale().indexOf(scale.getRoot());
  }

}
