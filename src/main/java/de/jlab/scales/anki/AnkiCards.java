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
import static de.jlab.scales.theory.Scales.allKeys;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  
  public Deck spellAllScales() {
    return spellScales(allKeys(commonScales()));
  }

  public Deck spellMajorScales() {
    Map<Note, Accidental> majorKeyAccidentals = new HashMap<>();
    Deck deck = new Deck();
    for (Scale scale : allKeys(CMajor)) {
      KeySignature keySignature = KeySignature.fromScale(scale);
      deck.add(new ScaleCard(scale, keySignature));
      majorKeyAccidentals.put(scale.getRoot(), keySignature.getAccidental());
    }
    for (Scale scale : allKeys(CMelodicMinor)) {
      Note majorRoot = scale.getRoot().transpose(-2);
      KeySignature keySignature = KeySignature.fromScale(scale, majorRoot, majorKeyAccidentals.get(majorRoot));
      deck.add(new ScaleCard(scale, keySignature).withBothNames());
    }
    for (Scale scale : allKeys(CHarmonicMinor)) {
      System.out.println("harmonic minor");
      Note majorRoot = scale.getRoot().transpose(3);
      KeySignature keySignature = KeySignature.fromScale(scale, majorRoot, majorKeyAccidentals.get(majorRoot));
      deck.add(new ScaleCard(scale, keySignature).withBothNames());
    }
    for (Scale scale : allKeys(CHarmonicMajor)) {
      System.out.println("harmonic major");
      Note majorRoot = scale.getRoot().transpose(0);
      KeySignature keySignature = KeySignature.fromScale(scale, majorRoot, majorKeyAccidentals.get(majorRoot));
      deck.add(new ScaleCard(scale, keySignature));
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
