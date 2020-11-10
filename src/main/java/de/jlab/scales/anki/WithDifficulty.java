package de.jlab.scales.anki;

public interface WithDifficulty {
  default int getDifficulty() {
    return 0;
  }
}
