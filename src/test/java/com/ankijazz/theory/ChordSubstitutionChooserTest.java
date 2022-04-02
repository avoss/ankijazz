package com.ankijazz.theory;

import static com.ankijazz.theory.ScaleUniverse.SCALES;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.theory.BuiltinChordType;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.PentatonicChooser;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;
import com.ankijazz.theory.ChordSubstitutionChooser.SubstitutionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChordSubstitutionChooserTest {
  
  @lombok.Data
  static class Documentation {
    private final String chordName;
    private final String pentaName;
    private final String commonNotes;
    private final String extraNotes;
    private final String missingNotes;
  }

  @Test
  public void testPentatonicMapping() {
    PentatonicChooser chooser = new PentatonicChooser();
    List<String> fullPentatonics = new ArrayList<>();
    List<String> bestPentatonics = new ArrayList<>();
    List<Documentation> documentation = new ArrayList<>();
    for (BuiltinChordType type : BuiltinChordType.values()) {
      if (type.getPrototype().getNumberOfNotes() < 4) {
        continue;
      }
      Scale chord = type.getPrototype();
      Optional<SubstitutionInfo> best = chooser.chooseBestInfo(chord);
      String title = String.format("*** Pentatonic for C%s = %s", type.getTypeName(), best.isPresent() ? SCALES.findFirstOrElseThrow(best.get().getSubstitution()).getScaleName() : "NOT FOUND");
      fullPentatonics.add(title);
      bestPentatonics.add(title);

      List<SubstitutionInfo> byQuality = chooser.orderedByQuality(chord);
      for (SubstitutionInfo substInfo : byQuality) {
        ScaleInfo pentInfo = SCALES.findFirstOrElseThrow(substInfo.getSubstitution());
        
        Set<Note> commonNotes = substInfo.getCommonNotes();
        Set<Note> extraNotes = substInfo.getExtraNotes();
        Set<Note> missingNotes = substInfo.getMissingNotes();
        
        String line = String.format("%30s quality: %d, common: %d (%s), extra: %d (%s), missing: %d (%s)", pentInfo.getScaleName(), substInfo.getQuality(), 
            commonNotes.size(), substInfo.getCommonNotesAsIntervals(), 
            extraNotes.size(), substInfo.getExtraNotesAsIntervals(), 
            missingNotes.size(), substInfo.getMissingNotesAsIntervals());
        fullPentatonics.add(line);
        if (best.get().equals(substInfo)) {
          documentation.add(new Documentation("C" + type.getTypeName(), pentInfo.getScaleName(), substInfo.getCommonNotesAsIntervals(), substInfo.getExtraNotesAsIntervals(), substInfo.getMissingNotesAsIntervals()));
        }
      }
    }
    //fullPentatonics.forEach(line -> System.out.println(line));
    TestUtils.assertFileContentMatches(bestPentatonics, getClass(), "BestPentatonics.txt");
    TestUtils.assertFileContentMatches(fullPentatonics, getClass(), "FullPentatonics.txt");
    writeDocumentation(documentation);
  }

  private void writeDocumentation(List<Documentation> documentation) {
    try {
      Path dir = Paths.get("build/preview");
      Files.createDirectories(dir);
      Path path = dir.resolve("PentatonicSubstitutions.json");
      new ObjectMapper().writeValue(path.toFile(), documentation);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }


}
