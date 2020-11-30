package de.jlab.scales.lily;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.anki.WithAssets;

public class LilyMetronome implements WithAssets {
  
  @lombok.Getter
  @lombok.RequiredArgsConstructor
  @lombok.EqualsAndHashCode
  public class Tempo {
    private final int bpm;
    private final String mp3Name;
    public int getMinBpm() {
      return LilyMetronome.this.minBpm;
    }
    public int getMaxBpm() {
      return LilyMetronome.this.maxBpm;
    }
  }
  
  private final int minBpm;
  private final int maxBpm;
  private final Map<String, String> templates = new HashMap<>();
  private final int granularity;
  private final String template;
  
  public LilyMetronome(int granularity, int minBpm, int maxBpm) {
    this.granularity = granularity;
    this.minBpm = minBpm;
    this.maxBpm = maxBpm;
    this.template = readTemplate();
  }

  public int getMaxBpm() {
    return maxBpm;
  }
  
  public int getMinBpm() {
    return minBpm;
  }
  
  public Tempo tempo(int bpm) {
    bpm = Math.min(maxBpm, bpm);
    bpm = Math.max(minBpm, bpm);
    bpm = (bpm / granularity) * granularity;
    String replaced = template.replace("${bpm}", Integer.toString(bpm));
    String assetId = Utils.assetId(replaced);
    templates.put(assetId, replaced);
    return new Tempo(bpm, assetId + ".mp3");
  }

  @Override
  public void writeAssets(Path dir) {
    for (String assetId : templates.keySet()) {
      writeAsset(dir, assetId + ".ly", templates.get(assetId));
    }
  }

  // TODO move to Utils or similar
  private void writeAsset(Path dir, String fileName, String content) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve(fileName);
      Files.write(path, Collections.singleton(content));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  // TODO move to Utils or similar
  private String readTemplate() {
    try (InputStream input = LilyRhythm.class.getResourceAsStream("LilyMetronome.ly")) {
      return Utils.readString(input);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
