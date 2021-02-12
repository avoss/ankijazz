package de.jlab.scales.midi.song;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongWrapper {
  private final Song song;
  private final String progressionSet;
  private final String progression;
  private final String key;
  private final String comment;
  
  public String getComment() {
    return comment == null ? "" : comment;
  }
}
