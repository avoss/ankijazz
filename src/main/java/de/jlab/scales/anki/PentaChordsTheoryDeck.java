package de.jlab.scales.anki;

import static de.jlab.scales.theory.ScaleUniverse.*;
import static de.jlab.scales.theory.ScaleUniverse.PENTAS;
import static de.jlab.scales.theory.Scales.CMajor;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.jlab.scales.difficulty.Difficulties;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class PentaChordsTheoryDeck extends AbstractDeck<SimpleCard> {

  private static final String FRONT = "front";
  private static final String BACK = "back";
  private static final String CHORD_NAME = "chordName";
  private static final String CHORD_TYPE = "chordType";

  public PentaChordsTheoryDeck() {
    super("Outline Chords with Pentatonic Scales");
    ChordScaleAudio.pentatonicChords().stream().flatMap(this::createCards).forEach(card -> add(card));
  }

  private Stream<SimpleCard> createCards(ChordScaleAudio csa) {
    return Arrays.stream(Note.values()).flatMap(n -> createCards(n.ordinal(), csa));
  }

  private Stream<SimpleCard> createCards(int semitones, ChordScaleAudio csa) {
    List<SimpleCard> cards = new ArrayList<SimpleCard>();
    Scale chord = csa.getChord().transpose(semitones);
    Scale penta = csa.getScale().transpose(semitones);
    String pentaType = PENTAS.findFirstOrElseThrow(penta).getTypeName();
    String chordType = CHORDS.findFirstOrElseThrow(chord).getTypeName();
    Set<Accidental> accidentals = SCALES.findScalesContaining(chord.asSet()).stream().map(info -> info.getKeySignature().getAccidental()).collect(Collectors.toSet());
    for (Accidental accidental : accidentals) {
      String chordName = chord.getRoot().getName(accidental).concat(chordType);
      String front = format("<div>Outline <b>%s</b></div>", chordName);
      String back = format("<div><b>%s %s</b></div>", penta.getRoot().getName(accidental), pentaType);
      SimpleCard card = new SimpleCard(Difficulties.getChordDifficulty(csa.getChord()), FRONT, BACK, CHORD_NAME, CHORD_TYPE);
      card.put(FRONT, front);
      card.put(BACK, back);
      card.put(CHORD_NAME, chordName);
      card.put(CHORD_TYPE, chordType);
      cards.add(card);
    }
    return cards.stream();
  }

}
