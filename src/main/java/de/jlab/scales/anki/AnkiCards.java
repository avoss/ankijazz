package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.allKeys;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleParser;
import de.jlab.scales.theory.ScaleUniverse;

public class AnkiCards {

  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor);

  public static class Deck {
    List<String> cards = new ArrayList<>();

    void add(String question, String answer) {
      cards.add(format("%s;%s", question, answer));
    }

    public void writeTo(Path path) {
      try {
        Collections.shuffle(cards);
        Files.write(path, cards);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    public List<String> getCards() {
      return cards;
    }
  }

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
      deck.add(scaleInfo.getName(), parentInfo.getName());
    }
    return deck;
  }

  public Deck spellAllScales() {
    return spellScales(allKeys(commonScales()));
  }
  
  private Deck spellScales(List<Scale> scales) {
    Deck deck = new Deck();
    for (Scale scale : scales) {
      addScale(deck, scale);
    }
    return deck;
  }

  private void addScale(Deck deck, Scale scale) {
    ScaleInfo scaleInfo = universe.info(scale);
    if (scale.getRoot() == Note.Gb) {
      deck.add(scaleInfo.getName(), scale.asScale(FLAT));
      deck.add(scaleInfo.getName(), scale.asScale(SHARP));
    } else {
      deck.add(scaleInfo.getName(), scale.asScale(scaleInfo.getAccidental()));
    }
  }

  public Deck spellMajorScales() {
    return spellScales(allKeys(asList(CMajor)));
  }

  public Deck spellMajorTriads() {
    return spellScales(allKeys(asList(CmajTriad)));
  }
  
  public Deck spellTypes() {
    Deck deck = new Deck();
    for (Scale scale : commonScales()) {
      ScaleInfo scaleInfo = universe.info(scale);
      deck.add(scaleInfo.getTypeName(), scale.asIntervals());
    }
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
    return scales;
  }


}
