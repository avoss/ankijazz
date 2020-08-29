package de.jlab.scales.theory;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

@lombok.RequiredArgsConstructor
@lombok.Getter 
@lombok.EqualsAndHashCode
@lombok.ToString
public class KeySignature {
  private final Note majorKey;
  private final Accidental accidental;
  private final int numberOfAccidentals;
  private final Map<Note, String> notationMap;
  
  
  public Accidental getAccidental() {
    return accidental;
  }
  public Note getMajorKey() {
    return majorKey;
  }
  
  /**
   * Works for Major, MelodicMinor, and Harmonic Minor. For their respective modes it also works but comes 
   * up with results that differ from their parent scale.
   * 
   * For scales with numberOfNotes != 7 a fallback keysignature (all notes flat) will be returned
   */
  public static KeySignature fromScale(Scale scale) {
    Analyzer analyzer = new Analyzer();
    Analyzer.Result result = analyzer.analyze(scale);
    return toKeySignature(result);
  }

  /**
   * if a key signatulre could be constructed, no matter how bad, then it is returned. Otherwise returns null.
   */
  public static KeySignature fromScale(Scale scale, Accidental accidental) {
    Analyzer analyzer = new Analyzer();
    Analyzer.Result result = analyzer.analyze(scale, accidental);
    if (!result.getRemainingScaleNotes().isEmpty()) {
      return null;
    }
    return toKeySignature(result);
  }
  
  private static KeySignature toKeySignature(Analyzer.Result result) {
    return new KeySignature(result.getRoot(), result.getAccidental(), result.getNumberOfAccidentalsInKeySignature(), result.getNotationMap());
  }

  public String notateKey() {
    return notate(majorKey);
  }
  
  public String notate(Note note) {
    return notationMap.get(note);
  }
  
  public List<String> notate(Scale scale) {
    return scale.asList().stream().map(this::notate).collect(toList());
  }
  
  public String toString(Scale scale) {
    return scale.asList().stream().map(this::notate).collect(joining(" "));
  }

}
