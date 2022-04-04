package com.ankijazz.theory;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
@EqualsAndHashCode
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
  
  /**
   * describes the parent scale. E.g. the parent scale of D-Dorian is C-Major
   */
  @EqualsAndHashCode.Exclude
  private ScaleInfo parentInfo;
  
  /**
   * KeySignature to use for notation. If a scale has multiple KeySignatures (like Gb/F# Major), then ScaleUniverse contains a ScaleInfo for each of them. 
   */
  private KeySignature keySignature;
  
  /**
   * e.g. if this ScaleInfo describes A-Minor Pentatonic scale, then superScales would contain all scales that contain the A-Minor Pentatonic 
   */
  private final List<Scale> superScales = new ArrayList<>();
  
  /*
   * e.g. if this ScaleInfo describes the C-Major scale, then subScales could contain the Pentatonic Scales included in the C-Major scale.
   */
  private final List<Scale> subScales = new ArrayList<>();
  private ScaleType scaleType;

  public boolean isInversion() {
    return !scale.equals(parentInfo.getScale());
  }

  public int getModeIndex() {
    return parentInfo.getScale().indexOf(scale.getRoot());
  }

  @Override
  public String toString() {
    return "ScaleInfo [scaleName=" + scaleName + ", typeName=" + typeName + ", scale=" + scale + ", parentInfo=" + parentInfo.getTypeName() + ", keySignature=" + keySignature + ", superScales="
        + superScales + ", subScales=" + subScales + ", scaleType=" + scaleType + "]";
  }
  

}
