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

  public Deck tritones() {
    Deck deck = new SimpleDeck("Tritones");
    for (Note note : Note.values()) {
      Note tritone = note.tritone();
      deck.add(note.getName(FLAT), tritone.getBothNames());
      if (!CMajor.contains(note))
        deck.add(note.getName(SHARP), tritone.getBothNames());
    }
    return deck;
  }

  public Deck parentScales() {
    Deck deck = new SimpleDeck("ParentScales");
    for (Scale scale : allKeys(commonModes(false))) {
      ScaleInfo scaleInfo = universe.info(scale);
      if (!scaleInfo.isInversion()) {
        continue;
      }
      ScaleInfo parentInfo = scaleInfo.getParentInfo();
      deck.add(scaleInfo.getScaleName(), parentInfo.getScaleName());
    }
    return deck;
  }

  public Deck spellTypes() {
    Deck deck = new SimpleDeck("ScaleTypes");
    IntervalAnalyzer analyzer = new IntervalAnalyzer();
    for (Scale scale : commonModes()) {
      ScaleInfo scaleInfo = universe.info(scale);
      Result result = analyzer.analyze(scale);
      deck.add(scaleInfo.getTypeName(), result.toString());
    }
    return deck;
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
        int difficulty = info.getKeySignature().getNumberOfAccidentals();
        difficulty += info.getScaleType() == BuiltInScaleTypes.Major ? 0 : 4;
        difficulty += info.isInversion() ? 3 : 0;
        deck.add(new ScaleCard(info, difficulty, false));
        if (includeDescending) {
          deck.add(new ScaleCard(info, difficulty, true));
        }
      }
    }
    return deck;
  }
  
  private Collection<Scale> commonScales() {
    List<Scale> scales = new ArrayList<>();
    scales.add(CMajor);
    scales.add(CMelodicMinor);
    scales.add(CHarmonicMinor);
//    scales.add(CHarmonicMajor);
    scales.add(CWholeTone);
    scales.add(CDiminishedHalfWhole);
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
