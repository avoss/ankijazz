package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.CMajor;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class DegreeParser {
  
  private final Pattern pattern = Pattern.compile("([b#x]*)([0-9]+)");

  @lombok.Data
  public static class Degree {
    private final int noteIndexInScale;
    private final int offsetToApply;
    
    public String toString() {
      return Accidental.fromOffset(offsetToApply).symbol() + (noteIndexInScale + 1);
    }
  }
  
  @lombok.Data
  public static class Degrees {
    private final List<Degree> degrees;
    private final Scale scale;

    public Degrees(List<Degree> degrees, Scale scale) {
      this.degrees = degrees;
      this.scale = scale.transpose(Note.C);
    }
   
    public String toString() {
      return degrees.stream().map(Degree::toString).collect(Collectors.joining(" "));
    }

    public Degrees relativeTo(Scale scale) {
      Scale normalized = scale.transpose(Note.C);
      List<Degree> list = degrees.stream().map(degree -> adjustOffset(degree, normalized)).collect(toList());
      return new Degrees(list, normalized);
    }

    private Degree adjustOffset(Degree degree, Scale newScale) {
      Note oldNote = scale.getNote(degree.noteIndexInScale);
      Note newNote = newScale.getNote(degree.noteIndexInScale);
      int newOffset = adjustOffset(degree.offsetToApply - oldNote.semitones(newNote));
      return new Degree(degree.noteIndexInScale, newOffset);
    }

    private int adjustOffset(int offset) {
      return offset > 6 ? offset - 12 : (offset < -6 ? offset + 12 : offset);
    }

    public Scale apply(Note root) {
      Scale transposed = scale.transpose(root);
      List<Note> notes = degrees.stream().map(d -> transposed.getNote(d.noteIndexInScale).transpose(d.offsetToApply)).collect(toList());
      return new Scale(notes.get(0), notes);
    }

  }

  public Degrees parse(String string) {
    return new Degrees(Arrays.stream(string.split(" +")).map(this::toDegree).collect(toList()), CMajor);
  }
  
  private Degree toDegree(String string) {
    Matcher matcher = pattern.matcher(string);
    if (!matcher.find()) {
      throw new IllegalArgumentException("unable to parse " + string);
    }
    int accidentalsToApply = 0;
    for (char c : matcher.group(1).toCharArray()) {
      switch (c) {
      case 'b': accidentalsToApply -= 1; break;
      case '#': accidentalsToApply += 1; break;
      case 'x': accidentalsToApply += 2; break;
      default: throw new IllegalArgumentException("unknown accidental in: " + string);
      }
    }
    int indexInScale = Integer.parseInt(matcher.group(2)) - 1;
    return new Degree(indexInScale, accidentalsToApply);
  }

}
