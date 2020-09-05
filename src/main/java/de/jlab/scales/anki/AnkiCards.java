package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.*;
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
import de.jlab.scales.theory.BuiltInScaleTypes;
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
    for (Scale scale : allKeys(commonModes())) {
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
    for (Scale scale : commonModes()) {
      ScaleInfo scaleInfo = universe.info(scale);
      deck.add(scaleInfo.getTypeName(), scale.asIntervals());
    }
    return deck;
  }

  public Deck spellScales() {
    return spellScales(commonScales(), new SimpleDeck("Scales"), false);
  }
  
  public Deck spellGuitarScales() {
    return spellScales(commonScales(), new GuitarDeck(new SimpleDeck("ScalesG")), true);
  }

  public Deck spellModes() {
    return spellScales(commonModes(), new SimpleDeck("Modes"), false);
  }
  
  public Deck spellGuitarModes() {
    return spellScales(commonModes(), new GuitarDeck(new SimpleDeck("ModesG")), true);
  }
  
  private Deck spellScales(Collection<Scale> scales, Deck deck, boolean includeDescending) {
    for (Scale scale : allKeys(scales)) {
      ScaleInfo info = universe.info(scale);
      int difficulty = info.getKeySignature().getNumberOfAccidentals();
      difficulty += info.getScaleType() == BuiltInScaleTypes.Major ? 0 : 4;
      difficulty += info.isInversion() ? 3 : 0;
      deck.add(new ScaleCard(scale, info.getKeySignature(), difficulty, false));
      if (includeDescending) {
        deck.add(new ScaleCard(scale, info.getKeySignature(), difficulty, true));
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
    return scales;
  }

  private Collection<Scale> commonModes() {
    List<Scale> scales = new ArrayList<>();
    scales.addAll(CMajor.getInversions());
    scales.add(CMelodicMinor);
    scales.add(CMelodicMinor.superimpose(F)); // Lydian Dominant
    scales.add(CMelodicMinor.superimpose(B)); // Altered
    scales.add(CHarmonicMinor);
    scales.add(CHarmonicMinor.superimpose(G)); // Phrygian Dominant
//    scales.add(CHarmonicMajor);
//    scales.add(CHarmonicMajor.transpose(B));   // Marcus
    return scales;
  }

}
