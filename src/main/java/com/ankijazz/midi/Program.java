package com.ankijazz.midi;

public enum Program {
  AcousticGrandPiano(0),
  BrightAcousticPiano(1),
  ElectricGrandPiano(2),
  HonkytonkPiano(3),
  ElectricPiano1(4),
  ElectricPiano2(5),
  Harpsichord(6),
  Clavinet(7),
  Celesta(8),
  Glockenspiel(9),
  MusicBox(10),
  Vibraphone(11),
  Marimba(12),
  Xylophone(13),
  TubularBells(14),
  Dulcimer(15),
  DrawbarOrgan(16),
  PercussiveOrgan(17),
  RockOrgan(18),
  ChurchOrgan(19),
  ReedOrgan(20),
  Accordion(21),
  Harmonica(22),
  TangoAccordion(23),
  AcousticGuitar_nylon(24),
  AcousticGuitar_steel(25),
  ElectricGuitar_jazz(26),
  ElectricGuitar_clean(27),
  ElectricGuitar_muted(28),
  OverdrivenGuitar(29),
  DistortionGuitar(30),
  GuitarHarmonics(31),
  AcousticBass(32),
  FingeredElectricBass(33),
  PickedElectricBass(34),
  FretlessBass(35),
  SlapBass1(36),
  SlapBass2(37),
  SynthBass1(38),
  SynthBass2(39),
  Violin(40),
  Viola(41),
  Cello(42),
  Contrabass(43),
  TremoloStrings(44),
  PizzicatoStrings(45),
  OrchestralHarp(46),
  Timpani(47),
  StringEnsemble1(48),
  StringEnsemble2(49),
  SynthStrings1(50),
  SynthStrings2(51),
  ChoirAahs(52),
  VoiceOohs(53),
  SynthVoice(54),
  OrchestraHit(55),
  Trumpet(56),
  Trombone(57),
  Tuba(58),
  MutedTrumpet(59),
  FrenchHorn(60),
  BrassSection(61),
  SynthBrass1(62),
  SynthBrass2(63),
  SopranoSax(64),
  AltoSax(65),
  TenorSax(66),
  BaritoneSax(67),
  Oboe(68),
  EnglishHorn(69),
  Bassoon(70),
  Clarinet(71),
  Piccolo(72),
  Flute(73),
  Recorder(74),
  PanFlute(75),
  BlownBottle(76),
  Shakuhachi(77),
  Whistle(78),
  Ocarina(79),
  Lead1_square(80),
  Lead2_sawtooth(81),
  Lead3_calliope(82),
  Lead4_chiff(83),
  Lead5_charang(84),
  Lead6_voice(85),
  Lead7_fifths(86),
  Lead8_bassLead(87),
  Pad1_newage(88),
  Pad2_warm(89),
  Pad3_polysynth(90),
  Pad4_choir(91),
  Pad5_bowed(92),
  Pad6_metallic(93),
  Pad7_halo(94),
  Pad8_sweep(95),
  FX1_train(96),
  FX2_soundtrack(97),
  FX3_crystal(98),
  FX4_atmosphere(99),
  FX5_brightness(100),
  FX6_goblins(101),
  FX7_echoes(102),
  FX8_scifi(103),
  Sitar(104),
  Banjo(105),
  Shamisen(106),
  Koto(107),
  Kalimba(108),
  Bagpipe(109),
  Fiddle(110),
  Shanai(111),
  TinkleBell(112),
  Agogo(113),
  SteelDrums(114),
  Woodblock(115),
  TailoDrum(116),
  MelodicDrum(117),
  SynthDrum(118),
  ReverseCymbal(119),
  GuitarFretNoise(120),
  BreathNoise(121),
  Seashore(122),
  BirdTweet(123),
  TelephoneRing(124),
  Helicopter(125),
  Applause(126),
  Gunshot(127);
  
  private int midiProgram;

  private Program(int midiProgram) {
    this.midiProgram = midiProgram;
  }
  public int getMidiProgram() {
    return midiProgram;
  }
}