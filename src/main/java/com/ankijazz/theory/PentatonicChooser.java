package com.ankijazz.theory;

import static com.ankijazz.theory.BuiltinScaleType.HarmonicMinor;
import static com.ankijazz.theory.BuiltinScaleType.Major;
import static com.ankijazz.theory.BuiltinScaleType.MelodicMinor;
import static com.ankijazz.theory.BuiltinScaleType.Minor6Pentatonic;
import static com.ankijazz.theory.BuiltinScaleType.Minor7Pentatonic;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.ankijazz.theory.ChordSubstitutionChooser.SubstitutionInfo;

public class PentatonicChooser {
  private static final ScaleUniverse universe = new ScaleUniverse(false, List.of(Major, MelodicMinor, HarmonicMinor, Minor7Pentatonic, Minor6Pentatonic));
  private final ChordSubstitutionChooser chooser = new ChordSubstitutionChooser();

  public Scale chooseBest(Scale chord) {
    return chooseBestInfo(chord).get().getSubstitution();
  }
  
  public Optional<SubstitutionInfo> chooseBestInfo(Scale chord) {
    return chooser.chooseBest(chord, candidates(chord));
  }

  public List<SubstitutionInfo> orderedByQuality(Scale chord) {
    return chooser.orderedByQuality(chord, candidates(chord));
  }
  
  private Set<Scale> candidates(Scale chord) {
    return universe.findScalesContaining(chord.asSet()).stream().flatMap(info -> info.getSubScales().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
  }

}
