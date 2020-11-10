package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.DiminishedHalfWhole;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor7Pentatonic;
import static de.jlab.scales.theory.BuiltInScaleTypes.WholeTone;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;
import static de.jlab.scales.theory.Scales.commonScales;
import static java.lang.String.format;

import java.util.Collection;

import de.jlab.scales.theory.BuiltInScaleTypes;
import de.jlab.scales.theory.IntervalAnalyzer;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class AnkiCards {
  // TODO duplicated in ScaleCard
  
  public Deck learnModes() {
    Deck deck = new AbstractDeck("LearnModes");
    for (Scale scale : allKeys(commonModes(false))) {
      for (ScaleInfo info : MODES.infos(scale)) {
        int difficulty = difficulty(info);
        spellNotes(deck, difficulty, info);
        nameParent(deck, difficulty, info);
        nameMode(deck, difficulty, info);
      }
    }
    addScaleTypes(deck);
    //addEnharmonics(deck);
    return deck;
  }

  private void addEnharmonics(Deck deck) {
    final String pattern = "<div>What is the enharmonic equivalent of <b>%s</b>?</div>"; 
    final String tags = "Enharmonics";
    for (Note note : Note.values()) {
      if (!CMajor.contains(note)) {
        deck.add(0, format(pattern, note.getName(SHARP)), note.getName(FLAT), tags);
        deck.add(0, format(pattern, note.getName(FLAT)), note.getName(SHARP), tags);
      }
    }
  }

  void addScaleTypes(Deck deck) {
    IntervalAnalyzer analyzer = new IntervalAnalyzer();
    for (Scale scale : commonModes()) {
      ScaleInfo info = MODES.info(scale);
      int difficulty = difficulty(info);
      String front = format("<div>What are the intervals of <b>%s</b>?</div>", info.getTypeName());
      String back = analyzer.analyze(scale).toString();
      String tags = format("ModeIntervals %s", sanitizedTypeName(info));
      deck.add(difficulty, front, back, tags);
    }
  }

  private String sanitizedTypeName(ScaleInfo info) {
    return info.getTypeName().replace(" ", "");
  }

  private void nameMode(Deck deck, int difficulty, ScaleInfo scaleInfo) {
    if (!scaleInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = scaleInfo.getParentInfo();
    int modeIndex = scaleInfo.getModeIndex() + 1;
    String front = format("<div>What is mode <b>%d</b> of <b>%s</b>?</div>", modeIndex, parentInfo.getScaleName());
    String back = format("<div><b>%s</b></div>", scaleInfo.getScaleName());
    String tags = tags("NameMode", parentInfo);
    deck.add(difficulty, front, back, tags);
  }

  private void nameParent(Deck deck, int difficulty, ScaleInfo scaleInfo) {
    if (!scaleInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = scaleInfo.getParentInfo();
    String front = format("<div>What is the parent scale of <b>%s</b>?</div>", scaleInfo.getScaleName());
    String back = format("<div><b>%s</b></div>", parentInfo.getScaleName());
    String tags = tags("NameParent", scaleInfo);
    deck.add(difficulty, front, back, tags);
  }

  private void spellNotes(Deck deck, int difficulty, ScaleInfo info) {
    String front = format("<div>What are the notes of <b>%s</b>?</div>", info.getScaleName());
    String back = format("<div><b>%s</b></div>", info.getKeySignature().toString(info.getScale()));
    String tags = tags("SpellNotes", info);
    deck.add(difficulty, front, back, tags);
  }

  private String tags(String task, ScaleInfo info) {
    return format("%s %s %s", task, sanitizedTypeName(info), info.getScale().getRoot().getName(FLAT));
  }

  private int difficulty(ScaleInfo info) {
    int difficulty = info.getKeySignature().getNumberOfAccidentals();
    difficulty += info.getScaleType() == BuiltInScaleTypes.Major ? 0 : 4;
    difficulty += info.isInversion() ? 3 : 0;
    return difficulty;
  }
  

  public Deck playScales() {
    return playScales(commonScales(), new AbstractDeck("PlayScales"), false);
  }
  
  public Deck playGuitarScales() {
    return playScales(commonScales(), new GuitarDeck(new AbstractDeck("PlayScalesGuitar")), true);
  }

  public Deck playModes() {
    return playScales(commonModes(), new AbstractDeck("PlayModes"), false);
  }
  
  public Deck playGuitarModes() {
    return playScales(commonModes(), new GuitarDeck(new AbstractDeck("PlayModesGuitar")), true);
  }
  
  private Deck playScales(Collection<Scale> scales, Deck deck, boolean includeDescending) {
    for (Scale scale : allKeys(scales)) {
      for (ScaleInfo info : MODES.infos(scale)) {
//        deck.add(new ScaleModel(info, false));
//        if (includeDescending) {
//          deck.add(new ScaleModel(info, true));
//        }
      }
    }
    return deck;
  }
  

}
