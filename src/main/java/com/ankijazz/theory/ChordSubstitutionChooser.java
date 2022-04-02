package de.jlab.scales.theory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;

public class ChordSubstitutionChooser {

  @EqualsAndHashCode
  @Getter
  public class SubstitutionInfo implements Comparable<SubstitutionInfo> {
    private final Scale chord;
    private final Set<Note> substitutionNotes;
    private final Set<Note> commonNotes;
    private final int quality;
    private final Scale substitution;
    private final Set<Note> chordNotes;
    private final Set<Note> extraNotes;
    private final Set<Note> missingNotes;
    private final Note root;

    SubstitutionInfo(Scale chord, Scale substitution) {
      this.chord = chord;
      root = chord.getRoot();
      this.substitution = substitution;
      substitutionNotes = substitution.asSet();
      chordNotes = chord.asSet();
      
      commonNotes = chord.asSet();
      commonNotes.retainAll(substitution.asSet());

      extraNotes = new HashSet<>(substitutionNotes);
      extraNotes.removeAll(chordNotes);
      
      missingNotes = new HashSet<>(chordNotes);
      missingNotes.removeAll(substitutionNotes);
      
      quality = computeQuality();
    }

    private int computeQuality() {
      Note root = chord.getRoot();
      int result = commonNotes.size() * 100;
      result -= commonNotes.contains(root) ? 10 : 0;
      result -= commonNotes.contains(root.five()) ? 5 : 0;
      if (chord.isAlteredDominant()) {
        result += substitutionContains(5, root.major3(), root.minor7());
        result += substitutionContains(1, root.flat5(), root.sharp5(), root.flat9(), root.sharp9());
        result -= substitutionContains(20, root.nine());
        result -= substitutionContains(50, root.major7());
        result -= substitutionContains(5, root.five());
      } else if (chord.isDominant()) {
        result += substitutionContains(10, root.major3(), root.minor7());
        result -= substitutionContains(50, root.major7());
        result -= substitutionContains(5, root.minor3(), root.flat5(), root.sharp5(), root.flat9(), root.sharp9());
      } else if (chord.isMajor()) {
        result += substitutionContains(10, root.major3(), root.major7());
        result -= substitutionContains(50, root.minor3(), root.minor7());
      } else if (chord.isMinor()) {
        result += substitutionContains(10, root.minor3(), root.minor7());
        result -= substitutionContains(10, root.major3(), root.major7());
      } else if (chord.isSus4()) {
        result += substitutionContains(10, root.four(), root.minor7());
        result -= substitutionContains(10, root.minor3(), root.major3(), root.major7());
        result -= substitutionContains(5, root.flat5(), root.sharp5(), root.flat9(), root.sharp9());
      }
      return result;
    }

    private int substitutionContains(int weight, Note... notes) {
      return Arrays.stream(notes).filter(note -> substitutionNotes.contains(note)).mapToInt(note -> weight).sum();
    }

    @Override
    public int compareTo(SubstitutionInfo that) {
      return Integer.compare(that.quality, this.quality);
    }
    
    public String getCommonNotesAsIntervals() {
      return toIntervals(commonNotes);
    }
    
    public String getExtraNotesAsIntervals() {
      return toIntervals(extraNotes);
    }
    
    public String getMissingNotesAsIntervals() {
      return toIntervals(missingNotes);
    }
    
    private String toIntervals(Set<Note> scrambledNotes) {
      if (scrambledNotes.isEmpty()) {
        return "";
      }
      Set<Note> orderedNotes = new TreeSet<>(scrambledNotes);
      return new Scale(orderedNotes.iterator().next(), orderedNotes).asIntervals(root);
    }
    
  }

  public Optional<SubstitutionInfo> chooseBest(Scale chord, Collection<? extends Scale> candidates) {
    List<SubstitutionInfo> result = orderedByQuality(chord, candidates);
    if (result.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(result.get(0));
  }

  public List<SubstitutionInfo> orderedByQuality(Scale chord, Collection<? extends Scale> candidates) {
    return candidates.stream().map(candidate -> new SubstitutionInfo(chord, candidate)).collect(Collectors.toCollection(LinkedHashSet::new)).stream().sorted()
        .collect(Collectors.toList());
  }

}
