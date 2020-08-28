package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


public class Analyzer {
  private static final List<Note> sharpSignatureKeys = Arrays.asList(F, C, G, D, A, E, B);
  private static final List<Note> flatSignatureKeys = Arrays.asList(B, E, A, D, G, C, F);

  @lombok.Getter
  @lombok.EqualsAndHashCode
  @lombok.ToString
  static class Result {
    
    private Note root;
    private final Accidental accidental;
    private int numberOfAccidentalsInKeySignature;

    private final Set<Note> majorNotesWithoutAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithInverseAccidental = new HashSet<>();
    private final Set<Note> remainingMajorNotes = new LinkedHashSet<>();
    private final Set<Note> remainingScaleNotes = new LinkedHashSet<>();
    private final Map<Note, String> notationMap = new LinkedHashMap<>();

    Result(Accidental accidental) {
      this.accidental = accidental;
    }

    void initialize() {
      if (accidental == SHARP) {
        initialize(sharpSignatureKeys, (n) -> n.transpose(2));
      } else {
        initialize(flatSignatureKeys, (n) -> n.transpose(6));
      }
    }

    private void initialize(List<Note> signatureKeys, Function<Note, Note> transposer) {
      root = C;
      numberOfAccidentalsInKeySignature = 0;
      for (Note note : signatureKeys) {
        if (majorNotesWithAccidental.contains(note)) {
          root = transposer.apply(note);
          numberOfAccidentalsInKeySignature += 1;
        } else {
          break;
        }
      }
    }
    
    public int getBadness() {
      int numberOfExtraAccidentals = majorNotesWithAccidental.size() - numberOfAccidentalsInKeySignature;
      return numberOfAccidentalsInKeySignature 
          + numberOfExtraAccidentals * 10
          + majorNotesWithInverseAccidental.size() * 100
          + remainingScaleNotes.size() * 1000;
    }

  }

  Analyzer.Result analyze(Scale scale, Accidental accidental) {
    Note majorRoot = cMajorStartNote(scale, accidental);
    Analyzer.Result result = new Result(accidental);
    List<Note> cmajorNotes = CMajor.superimpose(majorRoot).asList();
    List<Note> scaleNotes = scale.asList();
    result.remainingScaleNotes.addAll(scaleNotes);

    for (int i = 0; i < cmajorNotes.size(); i++) {
      Note cmajorNote = cmajorNotes.get(i);
      Note scaleNote = scaleNotes.get(i);
      if (cmajorNote == scaleNote) {
        result.majorNotesWithoutAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.notationMap.put(scaleNote, cmajorNote.name());
      } else if (accidental.apply(cmajorNote) == scaleNote) {
        result.majorNotesWithAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.notationMap.put(scaleNote, cmajorNote.name() + accidental.symbol());
      } else if (accidental.inverse().apply(cmajorNote) == scaleNote) {
        result.majorNotesWithInverseAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.notationMap.put(scaleNote, cmajorNote.name() + accidental.inverse().symbol());
      } else {
        result.remainingMajorNotes.add(cmajorNote);
      }
    }
    result.initialize();
    return result;
  }

  Note cMajorStartNote(Scale scale, Accidental accidental) {
    Note note = scale.getRoot();
    if (!CMajor.contains(note)) {
      return accidental.inverse().apply(note);
    }
    return note;
  }
}