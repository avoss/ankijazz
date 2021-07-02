package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sus4;
import static de.jlab.scales.theory.BuiltinChordType.Major6;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinChordType.Minor6;
import static de.jlab.scales.theory.BuiltinChordType.Minor7;
import static de.jlab.scales.theory.BuiltinChordType.Minor7b5;

import java.util.List;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.DegreeParser.Degrees;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;

public class ChordNoteTrainerDeck extends AbstractDeck<ChordNoteTrainerCard> {

  protected ChordNoteTrainerDeck() {
    super("Chord note trainer");
    for (BuiltinChordType type : List.of(Major7, Minor7, Dominant7, Minor7b5, Minor6, Major6, Dominant7sus4, Dominant7flat9, Dominant7sharp5)) {
      createCards(type);
    }
  }

  private void createCards(BuiltinChordType type) {
    Degrees degrees = type.getDegrees();
    for (Note root : Note.values()) {
      for (KeySignature keySignature : type.getDegreesKeySignatures(root)) {
        for (int index = 0; index < degrees.getDegrees().size(); index++) {
          ChordNoteTrainerCard card = ChordNoteTrainerCard.builder()
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
