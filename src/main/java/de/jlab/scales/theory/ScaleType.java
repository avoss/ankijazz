package de.jlab.scales.theory;

public interface ScaleType {
  Scale getPrototype();
  String getScaleName();
  String[] getModeNames();
}
