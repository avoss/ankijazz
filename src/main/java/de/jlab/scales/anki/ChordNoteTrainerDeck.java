package de.jlab.scales.anki;

import java.util.List;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.DegreeParser.Degrees;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;

import static de.jlab.scales.theory.BuiltinChordType.*;

public class PlayChordNotesDeck extends AbstractDeck<PlayChordNotesCard> {

  protected PlayChordNotesDeck() {
    super("Play Chord Notes");
    for (BuiltinChordType type : List.of(Major7, Minor7, Dominant7, Minor7b5)) {
      createCards(type);
    }
  }

  private void createCards(BuiltinChordType type) {
    Degrees degrees = type.getDegrees();
    for (Note root : Note.values()) {
      for (KeySignature keySignature : type.getDegreesKeySignatures(root)) {
        for (int index = 0; index < degrees.getDegrees().size(); index++) {
          PlayChordNotesCard card = PlayChordNotesCard.builder()
          .degrees(degrees)
          .index(index)
          .keySignature(keySignature)
          .typeName(type.getTypeName())
          .root(root)
          .build();
          super.add(card);
        }
      }
    }
  }

}
