package de.jlab.scales.gn;

import java.nio.file.Path;

import de.jlab.scales.Utils;
import de.jlab.scales.anki.MustacheCard;

public class GnCard extends MustacheCard {

  private final GnSong song;
  private final Type type;
  private final Instrument instrument;

  public GnCard(GnSong song, Type type, Instrument instrument) {
    this.song = song;
    this.type = type;
    this.instrument = instrument;
  }
  
  public String getTitle() {
    return String.format("%s (%s)", song.name(), type.name());
  }
  
  public String getMp3Name() {
    return String.format("%s-%s.mp3", song.name(), type == Type.Song ? instrument.getSongSuffix() : instrument.getSoloSuffix());
  }

  @Override
  public int getDifficulty() {
    return 0;
  }

  @Override
  public void writeAssets(Path directory) {

  }
  
  public String getTags() {
    return Utils.tags(type.name(), instrument.name());    
  }
}
