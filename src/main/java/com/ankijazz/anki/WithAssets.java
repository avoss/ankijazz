package com.ankijazz.anki;

import java.nio.file.Path;

public interface WithAssets {
  String getAssetId();
  void writeAssets(Path directory);
}
