package de.jlab.scales.theory;

import static de.jlab.scales.theory.ScaleUniverse.SCALES;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.ChordSubstitutionChooser.SubstitutionInfo;

public class ChordSubstitutionChooserTest {


  @Test
  public void testPentatonicMapping() {
    PentatonicChooser chooser = new PentatonicChooser();
    List<String> actual = new ArrayList<>();
    for (BuiltinChordType type : BuiltinChordType.values()) {
      if (type.getPrototype().getNumberOfNotes() < 4) {
        continue;
      }
      Scale chord = type.getPrototype();
      Optional<Scale> best = chooser.chooseBest(chord);
      actual.add(String.format("*** Pentatonic for C%s = %s", type.getTypeName(), best.isPresent() ? SCALES.findFirstOrElseThrow(best.get()).getScaleName() : "NOT FOUND"));

      List<SubstitutionInfo> byQuality = chooser.orderedByQuality(chord);
      for (SubstitutionInfo substInfo : byQuality) {
        ScaleInfo pentInfo = SCALES.findFirstOrElseThrow(substInfo.getSubstitution());
        
        Set<Note> substNotes = substInfo.getSubstitutionNotes();
        Set<Note> commonNotes = substInfo.getCommonNotes();
        Set<Note> chordNotes = chord.asSet();

        Set<Note> extraNotes = new HashSet<>(substNotes);
        extraNotes.removeAll(chordNotes);

        Set<Note> missingNotes = new HashSet<>(chordNotes);
        missingNotes.removeAll(substNotes);
        
        String line = String.format("%30s quality: %d, common: %d (%s), extra: %d (%s), missing: %d (%s)", pentInfo.getScaleName(), substInfo.getQuality(), 
            commonNotes.size(), toIntervals(commonNotes), 
            extraNotes.size(), toIntervals(extraNotes), 
            missingNotes.size(), toIntervals(missingNotes));
        actual.add(line);
      }
    }
    actual.forEach(line -> System.out.println(line));
    TestUtils.assertFileContentMatches(actual, getClass(), "PentatonicChordSubstitutions.txt");
  }

  private String toIntervals(Set<Note> scrambledNotes) {
    if (scrambledNotes.isEmpty()) {
      return "";
    }
    Set<Note> orderedNotes = new TreeSet<>(scrambledNotes);
    return new Scale(orderedNotes.iterator().next(), orderedNotes).asIntervals(Note.C);
  }

}
