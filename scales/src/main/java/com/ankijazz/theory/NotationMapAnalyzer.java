package com.ankijazz.theory;

import static com.ankijazz.theory.Accidental.SHARP;
import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Scales.CMajor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;


/**
 * Computes a Map<Note, Accidental> to be used to notate the notes of a scale. Note that some scales
 * require both, flats and sharps (like G-Melodic Minor = G A Bb C D E F#), or double sharps (like G# Melodic Minor).
 * Only works for scales with 7 notes. Other scales will always use flats.
 */
public class NotationMapAnalyzer {
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
    
    /**
     * same as notation map but contains accidental that was applied to create the notation
     */
    private final Map<Note, Accidental> accidentalMap = new TreeMap<>();

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
      completeAccidentalMap();
    }

    private void completeAccidentalMap() {
      for (Note note : Note.values()) {
        if (!CMajor.contains(note)) {
          accidentalMap.putIfAbsent(note, accidental);
        } else {
          accidentalMap.putIfAbsent(note, Accidental.NONE);
        }
      }
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

    public int getNumberOfAccidentals() {
      return getMajorNotesWithAccidental().size() 
          + getMajorNotesWithInverseAccidental().size()
          + getMajorNotesWithDoubleAccidental().size() * 2;
    }

  }

  // TODO feature envy, move to Result
  public Result analyzeScale(Scale scale, Accidental accidental) {
    if (scale.getNumberOfNotes() != CMajor.getNumberOfNotes())  {
      return fallback(scale, accidental);
    }
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
        result.accidentalMap.put(scaleNote, Accidental.NONE);
      } else if (accidental.apply(cmajorNote) == scaleNote) {
        result.majorNotesWithAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.accidentalMap.put(scaleNote, accidental);
      } else if (accidental.inverse().apply(cmajorNote) == scaleNote) {
        result.majorNotesWithInverseAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.accidentalMap.put(scaleNote, accidental.inverse());
      } else if (accidental.twice().apply(cmajorNote) == scaleNote) {
        result.majorNotesWithDoubleAccidental.add(cmajorNote);
        result.remainingScaleNotes.remove(scaleNote);
        result.accidentalMap.put(scaleNote, accidental.twice());
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

  public Result fallback(Scale scale, Accidental accidental) {
    Result result = new NotationMapAnalyzer.Result(scale, accidental);
    result.initialize();
    return result;
  }

}