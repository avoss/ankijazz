package de.jlab.scales.midi;

public enum Drum {


  // http://computermusicresource.com/GM.Percussion.KeyMap.html
  // http://computermusicresource.com/MIDI.resources.html
  
  AcousticBassDrum(35),
  RideCymbal2(59),
  BassDrum1(36),
  HiBongo(60),
  SideStick(37),
  LowBongo(61),
  AcousticSnare(38),
  MuteHiConga(62),
  HandClap(39),
  OpenHiConga(63),
  ElectricSnare(40),
  LowConga(64),
  LowFloorTom(41),
  HighTimbale(65),
  ClosedHiHat(42),
  LowTimbale(66),
  HighFloorTom(43),
  HighAgogo(67),
  PedalHiHat(44),
  LowAgogo(68),
  LowTom(45),
  Cabasa(69),
  OpenHiHat(46),
  Maracas(70),
  LowMidTom(47),
  ShortWhistle(71),
  HiMidTom(48),
  LongWhistle(72),
  CrashCymbal1(49),
  ShortGuiro(73),
  HighTom(50),
  LongGuiro(74),
  RideCymbal1(51),
  Claves(75),
  ChineseCymbal(52),
  HiWoodBlock(76),
  RideBell(53),
  LowWoodBlock(77),
  Tambourine(54),
  MuteCuica(78),
  SplashCymbal(55),
  OpenCuica(79),
  Cowbell(56),
  MuteTriangle(80),
  CrashCymbal2(57),
  OpenTriangle(81),
  Vibraslap(58),
  MetronomeBell(34),
  MetronomeClick(33);
  
  private int midiPitch;
  private static final int MIDI_CHANNEL = 9;

  private Drum(int midiPitch) {
    this.midiPitch = midiPitch;
  }

  public int getMidiPitch() {
    return midiPitch;
  }

  public static int getMidiChannel() {
    return MIDI_CHANNEL;
  }
}
