package de.jlab.scales.anki;

import java.nio.file.Path;

public interface WithAssets {
  default void writeAssets(Path directory) {
    // empty
  }

}
