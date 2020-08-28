package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.*;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Scales.CMajor;
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
   * works for Major, MelodicMinor, and Harmonic Minor. For their respective modes it also works but comes 
   * up with results that differ from their parent scale. 
   */
  public static KeySignature fromScale(Scale scale) {
    if (scale.length() != CMajor.length()) { // everything relates to CMajor in this crazy music system
      throw new IllegalArgumentException("Must be 7 notes scale" + scale.toString());
    }
    
    Analyzer analyzer = new Analyzer();
    Analyzer.Result sharpResult = analyzer.analyze(scale, SHARP);
    Analyzer.Result flatResult = analyzer.analyze(scale, FLAT);
    Analyzer.Result best = sharpResult.getBadness() <= flatResult.getBadness() ? sharpResult : flatResult;
    if (!best.getRemainingScaleNotes().isEmpty()) {
      throw new IllegalArgumentException("could not find singature for " + scale + ", best was " + best);
    }
    return new KeySignature(best.getRoot(), best.getAccidental(), best.getNumberOfAccidentalsInKeySignature(), best.getNotationMap());
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
