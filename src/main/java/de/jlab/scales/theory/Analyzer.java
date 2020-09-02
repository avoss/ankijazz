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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


public class Analyzer {
  // TODO duplicated in ScaleUniverse?
  private static final List<Note> sharpSignatureKeys = Arrays.asList(F, C, G, D, A, E, B);
  private static final List<Note> flatSignatureKeys = Arrays.asList(B, E, A, D, G, C, F);

  @lombok.Getter
  @lombok.EqualsAndHashCode
  @lombok.ToString
  static class Result {
    
    private final Accidental accidental;
    private final Scale scale;
    
    private Note root;
    private int numberOfAccidentalsInKeySignature;

    private final Set<Note> majorNotesWithoutAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithInverseAccidental = new HashSet<>();
    private final Set<Note> majorNotesWithDoubleAccidental = new HashSet<>();
    private final Set<Note> remainingMajorNotes = new LinkedHashSet<>();
    private final Set<Note> remainingScaleNotes = new LinkedHashSet<>();
    private final Map<Note, String> notationMap = new LinkedHashMap<>();

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
      if (accidental == SHARP) {
        if (note == F && scale.contains(Gb) && !scale.contains(E)) {
          return "E#";
        }
        if (note == C && scale.contains(Db) && !scale.contains(B)) {
          return "B#";
        }
      }

      if (accidental == FLAT) {
        if (note == B && scale.contains(Bb) && !scale.contains(C)) {
          return "Cb";
        }
        if (note == E && scale.contains(Eb) && !scale.contains(F)) {
          return "Fb";
        }
      }
      return note.getName(accidental);
    }
    

    private void computeNotationKey(List<Note> signatureKeys, Function<Note, Note> transposer) {
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
    
//    public int getBadness() {
//      int numberOfExtraAccidentals = majorNotesWithAccidental.size() - numberOfAccidentalsInKeySignature;
//      return numberOfAccidentalsInKeySignature 
//          + numberOfExtraAccidentals * 10
//          + majorNotesWithInverseAccidental.size() * 20
//          + majorNotesWithDoubleAccidental.size() * 50
//          + remainingScaleNotes.size() * 1000;
//    }

  }

  public Result analyze(Scale scale, Accidental accidental) {
    Analyzer.Result result = new Result(scale, accidental);
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

  Note cMajorStartNote(Scale scale, Accidental accidental) {
    Note note = scale.getRoot();
    if (!CMajor.contains(note)) {
      return accidental.inverse().apply(note);
    }
    return note;
  }

//  public Result fallback(Scale scale, Accidental accidental) {
//    Result result = new Analyzer.Result(scale, accidental);
//    result.initialize();
//    return result;
//  }
//
//  public Result fallback(Scale scale) {
//    return fallback(scale, FLAT);
//  }
//
//  public Result analyze(Scale scale) {
//    if (scale.length() != CMajor.length()) {
//      return fallback(scale);
//    }
//    Result sharpResult = analyze(scale, SHARP);
//    Result flatResult = analyze(scale, FLAT);
//    Result best = sharpResult.getBadness() < flatResult.getBadness() ? sharpResult : flatResult;
//    if (!best.getRemainingScaleNotes().isEmpty()) {
//      best = fallback(scale);
//    }
//    return best;
//  }

}