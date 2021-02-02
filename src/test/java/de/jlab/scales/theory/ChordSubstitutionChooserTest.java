package de.jlab.scales.theory;

import static de.jlab.scales.theory.ScaleUniverse.SCALES;
import static org.junit.Assert.*;
import de.jlab.scales.theory.ChordSubstitutionChooser.SubstitutionInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;


public class ChordSubstitutionChooserTest {

  @Test
  public void test() {
    fail("Not yet implemented");
  }

  @Test
  public void printPentatonicRelations() {
    for (BuiltinChordType type : BuiltinChordType.values()) {
      Scale chord = type.getPrototype();
      System.out.println("\n*** " + type.getTypeName());
      Set<Scale> candidates = SCALES.findScalesContaining(chord.asSet())
          .stream()
          .flatMap(info -> info.getSubScales().stream())
          .collect(Collectors.toSet());
      ChordSubstitutionChooser chooser = new ChordSubstitutionChooser();
      List<SubstitutionInfo> byQuality = chooser.orderedByQuality(chord, candidates);
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
        System.out.println(line);
      }
    }
  }    
    private String toIntervals(Set<Note> notes) {
      if (notes.isEmpty()) {
        return "";
      }
      return new Scale(notes.iterator().next(), notes).asIntervals(Note.C);
    }
  
}
