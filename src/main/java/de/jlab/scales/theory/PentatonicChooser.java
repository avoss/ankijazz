package de.jlab.scales.theory;

import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.jlab.scales.theory.ChordSubstitutionChooser.SubstitutionInfo;

public class PentatonicChooser {
  private static final ScaleUniverse universe = new ScaleUniverse(false, List.of(Major, MelodicMinor, HarmonicMinor, Minor7Pentatonic, Minor6Pentatonic));
  private final ChordSubstitutionChooser chooser = new ChordSubstitutionChooser();

  public Optional<SubstitutionInfo> chooseBest(Scale chord) {
    return chooser.chooseBest(chord, candidates(chord));
  }

  public List<SubstitutionInfo> orderedByQuality(Scale chord) {
    return chooser.orderedByQuality(chord, candidates(chord));
  }
  
  private Set<Scale> candidates(Scale chord) {
    return universe.findScalesContaining(chord.asSet()).stream().flatMap(info -> info.getSubScales().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
  }

}
