package de.jlab.scales.anki;

import java.nio.file.Path;

public interface WithAssets {
  String getAssetId();
  void writeAssets(Path directory);
}
