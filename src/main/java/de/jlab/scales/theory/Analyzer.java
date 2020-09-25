package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;


/**
 * TODO only notationMap and remainingScaleNotes is used, everything else can be removed
 */
public class Analyzer {
  private static final List<Note> sharpSignatureKeys = Arrays.asList(F, C, G, D, A, E, B);
  private static final List<Note> flatSignatureKeys = Arrays.asList(B, E, A, D, G, C, F);
  
  @lombok.Getter
  @lombok.EqualsAndHashCode
  @lombok.ToString
  static class Result {
    
    private final Accidental accidental;
    private final Scale scale;
    
    private Note notationKey;
    private int numberOfAccidentalsInKeySignature;

    private final Set<Note> majorNotesWithoutAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithInverseAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithDoubleAccidental = new HashSet<>();
    private final Set<Note> remainingMajorNotes = new LinkedHashSet<>();
    private final Set<Note> remainingScaleNotes = new LinkedHashSet<>();
    private final Map<Note, String> notationMap = new TreeMap<>();

    Result(Scale scale, Accidental accidental) {
      this.scale = scale;
      this.accidental = accidental;
    }

    void initialize() {
      if (accidental == SHARP) {
        computeNotationKey(sharpSignatureKeys, (n) -> n.transpose(2));
      } else {
        computeNotationKey(flatSignatureKeys, (n) -> n.transpose(6));
      }
      putDefaultNotationIntoNotationMapForNotesNotInScale();
    }

    private void putDefaultNotationIntoNotationMapForNotesNotInScale() {
      for (Note note : Note.values()) {
        notationMap.putIfAbsent(note, defaultNotation(scale, note));
      }
    }
    
    private String defaultNotation(Scale scale, Note note) {
      return note.getName(accidental);
    }
    
    private void computeNotationKey(List<Note> signatureKeys, Function<Note, Note> transposer) {
      notationKey = C;
      numberOfAccidentalsInKeySignature = 0;
      for (Note note : signatureKeys) {
        if (majorNotesWithAccidental.contains(note)) {
          notationKey = transposer.apply(note);
          numberOfAccidentalsInKeySignature += 1;
        } else {
          break;
        }
      }
    }

  }

  public Result analyze(Scale scale, Accidental accidental) {
    Result result = new Result(scale, accidental);
    Note majorRoot = cMajorStartNote(scale, accidental);
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
      } else if (accidental.twice().apply(cmajorNote) == scaleNote) {
        result.majorNotesWithDoubleAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.notationMap.put(scaleNote, cmajorNote.name() + accidental.twice().symbol());
      } else {
        result.remainingMajorNotes.add(cmajorNote);
      }
    }
    result.initialize();
    return result;
  }

  private Note cMajorStartNote(Scale scale, Accidental accidental) {
    Note note = scale.getRoot();
    if (!CMajor.contains(note)) {
      return accidental.inverse().apply(note);
    }
    return note;
  }


}