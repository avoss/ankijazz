package de.jlab.scales.gn;

public enum Instrument {

  Guitar("NoGit", "SoloLoop"), Sax("NoSax", "SoloLoop");

  private final String songSuffix;
  private final String soloSuffix;

  Instrument(String songSuffix, String soloSuffix) {
    this.songSuffix = songSuffix;
    this.soloSuffix = soloSuffix;
  }

  public String getSoloSuffix() {
    return soloSuffix;
  }

  public String getSongSuffix() {
    return songSuffix;
  }

}
