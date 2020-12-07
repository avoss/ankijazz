package de.jlab.scales.gn;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.jlab.scales.anki.Card;

public class GnCard implements Card {

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
  public double getDifficulty() {
    return 0;
  }

  @Override
  public void writeAssets(Path directory) {

  }

  @Override
  public String getCsv() {
    return Stream.of(getTitle(), getMp3Name()).collect(Collectors.joining(";"));
  }
  
}
