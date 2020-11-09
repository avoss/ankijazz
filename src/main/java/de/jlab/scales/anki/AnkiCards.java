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
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CWholeTone;
import static de.jlab.scales.theory.Scales.allKeys;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jlab.scales.theory.BuiltInScaleTypes;
import de.jlab.scales.theory.IntervalAnalyzer;
import de.jlab.scales.theory.IntervalAnalyzer.Result;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class AnkiCards {
  // TODO duplicated in ScaleCard
  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor, HarmonicMajor, DiminishedHalfWhole, WholeTone, Minor7Pentatonic, Minor6Pentatonic);

  public Deck learnModes() {
    Deck deck = new SimpleDeck("LearnModes");
    for (Scale scale : allKeys(commonModes(false))) {
      for (ScaleInfo info : universe.infos(scale)) {
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
      ScaleInfo info = universe.info(scale);
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
    return playScales(commonScales(), new SimpleDeck("PlayScales"), false);
  }
  
  public Deck playGuitarScales() {
    return playScales(commonScales(), new GuitarDeck(new SimpleDeck("PlayScalesGuitar")), true);
  }

  public Deck playModes() {
    return playScales(commonModes(), new SimpleDeck("PlayModes"), false);
  }
  
  public Deck playGuitarModes() {
    return playScales(commonModes(), new GuitarDeck(new SimpleDeck("PlayModesGuitar")), true);
  }
  
  private Deck playScales(Collection<Scale> scales, Deck deck, boolean includeDescending) {
    for (Scale scale : allKeys(scales)) {
      for (ScaleInfo info : universe.infos(scale)) {
        deck.add(new ScaleCard(info, false));
        if (includeDescending) {
          deck.add(new ScaleCard(info, true));
        }
      }
    }
    return deck;
  }
  
  private Collection<Scale> commonScales() {
    return commonScales(true);
  }
  
  private Collection<Scale> commonScales(boolean includeSymmetricScales) {
    List<Scale> scales = new ArrayList<>();
    scales.add(CMajor);
    scales.add(CMelodicMinor);
    scales.add(CHarmonicMinor);
//    scales.add(CHarmonicMajor);
    if (includeSymmetricScales) {
      scales.add(CWholeTone);
      scales.add(CDiminishedHalfWhole);
    }
    return scales;
  }

  private Collection<Scale> commonModes() {
    return commonModes(true);
  }

  private Collection<Scale> commonModes(boolean includeSymmetricScales) {
    List<Scale> scales = new ArrayList<>();
    scales.addAll(CMajor.getInversions());
    scales.add(CMelodicMinor);
    scales.add(CMelodicMinor.superimpose(F)); // Lydian Dominant
    scales.add(CMelodicMinor.superimpose(B)); // Altered
    scales.add(CHarmonicMinor);
    scales.add(CHarmonicMinor.superimpose(G)); // Phrygian Dominant
//    scales.add(CHarmonicMajor);
//    scales.add(CHarmonicMajor.transpose(B));   // Marcus
    if (includeSymmetricScales) {
      scales.add(CWholeTone);
      scales.add(CDiminishedHalfWhole);
      scales.add(CDiminishedHalfWhole.superimpose(Db));
    }
    return scales;
  }

}
