package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.CHarmonicMajor;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.*;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class AnkiCards {

  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor, HarmonicMajor);

  public Deck tritones() {
    Deck deck = new Deck();
    for (Note note : Note.values()) {
      Note tritone = note.tritone();
      deck.add(note.getName(FLAT), tritone.getBothNames());
      if (!CMajor.contains(note))
        deck.add(note.getName(SHARP), tritone.getBothNames());
    }
    return deck;
  }

  public Deck parentScales() {
    Deck deck = new Deck();
    for (Scale scale : allKeys(commonScales())) {
      ScaleInfo scaleInfo = universe.info(scale);
      if (!scaleInfo.isInversion()) {
        continue;
      }
      ScaleInfo parentInfo = universe.info(scaleInfo.getParent());
      deck.add(scaleInfo.getScaleName(), parentInfo.getScaleName());
    }
    return deck;
  }

  public Deck spellTypes() {
    Deck deck = new Deck();
    for (Scale scale : commonScales()) {
      ScaleInfo scaleInfo = universe.info(scale);
      deck.add(scaleInfo.getTypeName(), scale.asIntervals());
    }
    return deck;
  }
  

  public Deck spellMajorScales() {
    Map<Scale, Function<Scale, List<Scale>>> modeMap = new HashMap<>();
    Stream.of(CMajor, CMelodicMinor, CHarmonicMinor, CHarmonicMajor).forEach(type -> modeMap.put(type, (parent) -> asList(parent)));
    return spellScales(modeMap);
  }

  public Deck spellAllScales() {
    Map<Scale, Function<Scale, List<Scale>>> modeMap = new HashMap<>();
    modeMap.put(CMajor, parent -> allModes(parent));
    modeMap.put(CMelodicMinor, parent -> asList(parent, parent.superimpose(F.ordinal()), parent.transpose(B.ordinal())));
    modeMap.put(CHarmonicMinor, parent -> asList(parent, parent.superimpose(G.ordinal())));
    modeMap.put(CHarmonicMajor, parent -> asList(parent, parent.superimpose(B.ordinal())));
    return spellScales(modeMap);
  }
  
  private Deck spellScales(Map<Scale, Function<Scale, List<Scale>>> modes) {
    Map<Note, Accidental> majorKeyAccidentals = new HashMap<>();
    Deck deck = new Deck();
    for (Scale parent : allKeys(CMajor)) {
      KeySignature keySignature = KeySignature.fromScale(parent);
      majorKeyAccidentals.put(parent.getRoot(), keySignature.getAccidental());
      for (Scale mode : modes.get(CMajor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature));
      }
    }
    for (Scale parent : allKeys(CMelodicMinor)) {
      Note majorRoot = parent.getRoot().transpose(-2);
      KeySignature keySignature = KeySignature.fromScale(parent, majorRoot, majorKeyAccidentals.get(majorRoot));
      for (Scale mode : modes.get(CMelodicMinor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature).withBothNames());
      }
    }
    for (Scale parent : allKeys(CHarmonicMinor)) {
      Note majorRoot = parent.getRoot().transpose(3);
      KeySignature keySignature = KeySignature.fromScale(parent, majorRoot, majorKeyAccidentals.get(majorRoot));
      for (Scale mode : modes.get(CHarmonicMinor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature).withBothNames());
      }
    }
    for (Scale parent : allKeys(CHarmonicMajor)) {
      Note majorRoot = parent.getRoot().transpose(0);
      KeySignature keySignature = KeySignature.fromScale(parent, majorRoot, majorKeyAccidentals.get(majorRoot));
      Function<Scale, List<Scale>> fun = modes.get(CHarmonicMajor);
      for (Scale mode : fun.apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature));
      }
    }
    return deck;
  }
  
  private Deck spellScales(List<Scale> scales) {
    Deck deck = new Deck();
    for (Scale scale : scales) {
      addScaleCard(deck, scale);
    }
    return deck;
  }

  private void addScaleCard(Deck deck, Scale scale) {
    ScaleInfo scaleInfo = universe.info(scale);
    KeySignature keySignature = scaleInfo.getKeySignature();
    deck.add(new ScaleCard(scale, keySignature));
  }


  private Collection<Scale> commonScales() {
    List<Scale> scales = new ArrayList<>();
    scales.addAll(CMajor.getInversions());
    scales.add(CMelodicMinor);
    scales.add(CMelodicMinor.superimpose(F)); // Lydian Dominant
    scales.add(CMelodicMinor.superimpose(B)); // Altered
    scales.add(CHarmonicMinor);
    scales.add(CHarmonicMinor.superimpose(G)); // Phrygian Dominant
    scales.add(CHarmonicMajor);
    scales.add(CHarmonicMajor.transpose(B));   // Marcus
    return scales;
  }


}
