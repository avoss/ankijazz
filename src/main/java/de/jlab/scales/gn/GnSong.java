package de.jlab.scales.gn;

enum GnSong {
  AfricanHeritage(true),
  AfterLongDay(true),
  BeatnPath(false),
  BluesMotion(true),
  CamelPanic(true),
  DontDance(false),
  DoWhatChaWanna(true),
  FunkAsUsual(false),
  Jazzmine(true),
  MelancolieAgite(true),
  Montuno(false),
  Progression(false),
  Random(true),
  Shutup(true),
  Siesta(false),
  Summertime(false),
  Sunny(true),
  TurningPages(false);
  
  private boolean guitarSolo;

  GnSong(boolean guitarSolo) {
    this.guitarSolo = guitarSolo;
  }

  public boolean hasGuitarSolo() {
    return guitarSolo;
  }
}