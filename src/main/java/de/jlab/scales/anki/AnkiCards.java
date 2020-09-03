package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.*;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.WholeTone;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CWholeTone;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.allModes;
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
    Deck deck = new SimpleDeck("ScaleTypes");
    for (Scale scale : commonScales()) {
      ScaleInfo scaleInfo = universe.info(scale);
      deck.add(scaleInfo.getTypeName(), scale.asIntervals());
    }
    return deck;
  }

  public Deck spellParentScales(boolean includeDescending) {
    Map<Scale, Function<Scale, List<Scale>>> modeMap = new HashMap<>();
    Stream.of(CMajor, CMelodicMinor, CHarmonicMinor, CHarmonicMajor, CMinorPentatonic, CMinor6Pentatonic).forEach(type -> modeMap.put(type, (parent) -> asList(parent)));
    Deck deck = new SimpleDeck("ParentScales");
    return spellScales(deck, modeMap, includeDescending);
  }

  public Deck spellAllScales(boolean includeDescending) {
    Map<Scale, Function<Scale, List<Scale>>> modeMap = new HashMap<>();
    modeMap.put(CMajor, parent -> allModes(parent));
    modeMap.put(CMelodicMinor, parent -> asList(parent, parent.superimpose(F.ordinal()), parent.superimpose(B.ordinal())));
    modeMap.put(CHarmonicMinor, parent -> asList(parent, parent.superimpose(G.ordinal())));
    modeMap.put(CHarmonicMajor, parent -> asList(parent, parent.superimpose(B.ordinal())));
    modeMap.put(CMinorPentatonic, parent -> asList(parent, parent.superimpose(Eb.ordinal())));
    modeMap.put(CMinor6Pentatonic, parent -> asList(parent, parent.superimpose(F.ordinal()), parent.superimpose(A.ordinal())));
    Deck deck = new SimpleDeck("AllScales");
    return spellScales(deck, modeMap, includeDescending);
  }
  
  private Deck spellScales(Deck deck, Map<Scale, Function<Scale, List<Scale>>> modes, boolean includeDescending) {
    for (Scale parent : allKeys(CMajor)) {
      Note majorKey = parent.getRoot().transpose(0);
      KeySignature keySignature = KeySignature.fromScale(parent, majorKey, Accidental.fromMajorKey(majorKey));
      for (Scale mode : modes.get(CMajor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature, false));
        if (includeDescending) {
          deck.add(new ScaleCard(mode, keySignature, true));
        }
      }
    }
    for (Scale parent : allKeys(CMelodicMinor)) {
      Note majorKey = parent.getRoot().transpose(-2);
      KeySignature keySignature = KeySignature.fromScale(parent, majorKey, Accidental.fromMajorKey(majorKey));
      for (Scale mode : modes.get(CMelodicMinor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature, false));
        if (includeDescending) {
          deck.add(new ScaleCard(mode, keySignature, true));
        }
      }
    }
    for (Scale parent : allKeys(CHarmonicMinor)) {
      Note majorKey = parent.getRoot().transpose(3);
      KeySignature keySignature = KeySignature.fromScale(parent, majorKey, Accidental.fromMajorKey(majorKey));
      for (Scale mode : modes.get(CHarmonicMinor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature, false));
        if (includeDescending) {
          deck.add(new ScaleCard(mode, keySignature, true));
        }
      }
    }
    for (Scale parent : allKeys(CHarmonicMajor)) {
      Note majorKey = parent.getRoot().transpose(0);
      KeySignature keySignature = KeySignature.fromScale(parent, majorKey, Accidental.fromMajorKey(majorKey));
      for (Scale mode : modes.get(CHarmonicMajor).apply(parent)) {
        deck.add(new ScaleCard(mode, keySignature, false));
        if (includeDescending) {
          deck.add(new ScaleCard(mode, keySignature, true));
        }
      }
    }
//    for (Scale scale : allKeys(CDiminishedHalfWhole)) {
//      KeySignature keySignature = KeySignature.fallback(scale, FLAT);
//      deck.add(new ScaleCard(scale, keySignature));
//      if (includeDescending) {
//        deck.add(new ScaleCard(scale, keySignature, true));
//      }
//    }
//    for (Scale scale : allKeys(CWholeTone)) {
//      KeySignature keySignature = KeySignature.fallback(scale, FLAT);
//      deck.add(new ScaleCard(scale, keySignature));
//      if (includeDescending) {
//        deck.add(new ScaleCard(scale, keySignature, true));
//      }
//    }
//    for (Scale parent : allKeys(CMinorPentatonic)) {
//      Note majorKey = parent.getRoot().transpose(3);
//      KeySignature keySignature = KeySignature.fromScale(CMajor.transpose(majorKey), majorKey, Accidental.fromMajorKey(majorKey));
//      for (Scale mode : modes.get(CMinorPentatonic).apply(parent)) {
//        deck.add(new ScaleCard(mode, keySignature));
//        if (includeDescending) {
//          deck.add(new ScaleCard(mode, keySignature, true));
//        }
//      }
//    }
//    for (Scale parent : allKeys(CMinor6Pentatonic)) {
//      Note majorKey = parent.getRoot().transpose(-2);
//      KeySignature keySignature = KeySignature.fromScale(CMajor.transpose(majorKey), majorKey, Accidental.fromMajorKey(majorKey));
//      for (Scale mode : modes.get(CMinor6Pentatonic).apply(parent)) {
//        deck.add(new ScaleCard(mode, keySignature));
//        if (includeDescending) {
//          deck.add(new ScaleCard(mode, keySignature, true));
//        }
//      }
//    }
    return deck;
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
