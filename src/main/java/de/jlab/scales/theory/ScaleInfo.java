package de.jlab.scales.theory;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleInfo {
  /**
   * scale: "Bb Dorian" because parent is Ab Major
   * chord: "Bbm7"
   */
  private String scaleName;
  /**
   * scale: "Dorian" 
   * chord: "m7"
   */
  private String typeName;
  private Scale scale;
  private Scale parent;
  private KeySignature keySignature;
  private final List<Scale> superScales = new ArrayList<>();
  private final List<Scale> subScales = new ArrayList<>();

  public boolean isInversion() {
    return !scale.equals(parent);
  }
  
  public int modeIndex() {
    return parent.indexOf(scale.getRoot());
  }

}
