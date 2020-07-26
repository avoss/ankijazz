package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.BuiltInScaleTypes.*;
import static de.jlab.scales.theory.Scales.*;
import static java.lang.String.format;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.jlab.scales.theory.Note;
import static de.jlab.scales.theory.Note.*;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class AnkiCards {

  private ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor);

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

  public Deck spellScales() {
    Deck deck = new Deck();
    for (Scale scale : allKeys(commonScales())) {
      ScaleInfo scaleInfo = universe.info(scale);
      deck.add(scaleInfo.getName(), scale.asScale(scaleInfo.getAccidental()));
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