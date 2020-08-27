package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.*;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@lombok.Data
@lombok.RequiredArgsConstructor(staticName = "of")
public class KeySignature {
  private final Note root;
  private final Accidental accidental;

  public static KeySignature fromScale(Scale scale) {
    if (scale.length() != CMajor.length()) { // everything relates to CMajor
      throw new IllegalArgumentException("Must be 7 notes scale" + scale.toString());
    }
    
    Analyzer analyzer = new Analyzer();
    Analyzer.Result sharpResult = analyzer.analyze(scale, SHARP);
    Analyzer.Result flatResult = analyzer.analyze(scale, FLAT);

//    int sharpFailures = tryAccidental(scale, SHARP);
//    int flatFailures = tryAccidental(scale, FLAT);
//    return sharpFailures == flatFailures ? SHARP : (sharpFailures < flatFailures ? SHARP : FLAT);
    return null;
  }

  static class Analyzer {
    private static final List<Note> sharpSignatureKeys = Arrays.asList(F, C, G, D, A, E, B);
    private static final List<Note> flatSignatureKeys = Arrays.asList(B, E, A, D, G, C, F);

    @lombok.Getter
    static class Result {
      
      private Note root;
      private final Accidental accidental;
      private int numberOfAccidentalsInKeySignature;

      private final Set<Note> notesWithoutAccidental = new HashSet<>();
      private final Set<Note> notesWithAccidental = new HashSet<>();
      private final Set<Note> notesWithInverseAccidental = new HashSet<>();
      private final Set<Note> notesWithDuplicateName = new HashSet<>();

      Result(Accidental accidental) {
        this.accidental = accidental;
      }

      public void initialize() {
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
          if (notesWithAccidental.contains(note)) {
            root = transposer.apply(note);
            numberOfAccidentalsInKeySignature += 1;
          } else {
            break;
          }
        }
      }
    }

    Result analyze(Scale scale, Accidental accidental) {
      Result result = new Result(accidental);
      Note majorRoot = scale.getRoot();
      if (!CMajor.contains(majorRoot)) {
        majorRoot = accidental.inverse().apply(majorRoot);
      }
      List<Note> cmajorNotes = CMajor.superimpose(majorRoot).asList();
      List<Note> scaleNotes = scale.asList();

      for (int i = 0; i < cmajorNotes.size(); i++) {
        Note cmajorNote = cmajorNotes.get(i);
        Note scaleNote = scaleNotes.get(i);
        if (cmajorNote == scaleNote) {
          result.notesWithoutAccidental.add(cmajorNote);
        } else if (accidental.apply(cmajorNote) == scaleNote) {
          result.notesWithAccidental.add(cmajorNote);
        } else if (accidental.inverse().apply(cmajorNote) == scaleNote) {
          result.notesWithInverseAccidental.add(cmajorNote);
        } else {
          result.notesWithDuplicateName.add(cmajorNote);
        }
      }
      result.initialize();
      return result;
    }
  }

}
