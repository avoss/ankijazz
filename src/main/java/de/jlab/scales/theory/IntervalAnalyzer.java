package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.stream.Collectors.joining;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class IntervalAnalyzer {
  
  @lombok.Getter
  @lombok.EqualsAndHashCode
  public static class Result {
    private final Map<Note, String> intervals = new TreeMap<>();
    private final Scale scale;
    
    public Result(Scale scale) {
      this.scale = scale;
    }

    @Override
    public String toString() {
      return scale.stream().map(intervals::get).collect(joining(" "));
    }
    
  }

  public Result analyze(Scale scale) {
    return analyze(scale, scale.getRoot());
  }
  
  public Result analyze(Scale scale, Note root) {
    Result result = new Result(scale);
    Set<Note> scaleNotes = scale.asSet();
    Scale major = CMajor.transpose(root);
    computeIntervalsRelativeToMajorScale(result, scaleNotes, major);
    if (!scaleNotes.isEmpty()) {
      return fallback(scale, root);
    }
    return result;
  }

  private void computeIntervalsRelativeToMajorScale(Result result, Set<Note> scaleNotes, Scale major) {
    for (int i = 0; i < major.getNumberOfNotes(); i++) {
      Note cMajorNote = major.getNote(i);
      String intervalName = Integer.toString(i + 1);
      if (i > 0 && scaleNotes.contains(cMajorNote.transpose(-1))) {
        result.intervals.put(cMajorNote.transpose(-1), "b" + intervalName);
        scaleNotes.remove(cMajorNote.transpose(-1));
      } else if (scaleNotes.contains(cMajorNote)) {
        result.intervals.put(cMajorNote, intervalName);
        scaleNotes.remove(cMajorNote);
      } else if (i != 5 // no #6 
          && scaleNotes.contains(cMajorNote.transpose(1))) {
        result.intervals.put(cMajorNote.transpose(1), "#" + intervalName);
        scaleNotes.remove(cMajorNote.transpose(1));
      }
    }
  }

  public Result fallback(Scale scale, Note root) {
    Result result = new Result(scale);
    scale.forEach(note -> {
      result.intervals.put(note, root.intervalName(note));
    });
    return result;
  }
}
