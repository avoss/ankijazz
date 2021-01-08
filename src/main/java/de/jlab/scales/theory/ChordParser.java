package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

public class ChordParser {
  private final Accidental accidental;

  private static final Pattern chordPattern = Pattern.compile("\\s*(C#|C|D#|Db|D|Eb|E|F#|F|G#|Gb|G|A#|Ab|A|Bb|B)\\s*(aug|\\+|dim7|o7|dim|o|7sus4|7sus|sus4|7sus2|sus2|sus|mmaj7|maj7|maj9|maj|m7|-7|m|-|7|m6|-6|6|5)?(.*)\\s*", Pattern.CASE_INSENSITIVE);
  private static final Pattern optionsPattern = Pattern.compile("(b|#)?(4|5|6|7|9|11|13)\\s*", Pattern.CASE_INSENSITIVE);
  
  public ChordParser(Accidental accidental) {
    this.accidental = accidental;
  }

  @SuppressWarnings("serial")
  private static final HashMap<String, Note> noteNameMap = new HashMap<String, Note>() {
    {
      for (Note note : Note.values()) {
        put(note.getName(Accidental.FLAT).toLowerCase(), note);
        put(note.getName(Accidental.SHARP).toLowerCase(), note);
      }
    }
  };


  public static Scale parseChord(String string) throws ParseChordException {
    string = string.replace("Δ7", "maj7"); // bug in regex?
    string = string.replace("Δ9", "maj9"); // bug in regex?
    Matcher matcher = chordPattern.matcher(string);
    if (!matcher.find()) {
      throw new ParseChordException("Not a chord: " + string);
    }
    Note root = noteNameMap.get(matcher.group(1).toLowerCase());
    if (root == null)
      throw new ParseChordException("Root not found: " + string);
    Set<Note> notes = new TreeSet<Note>();
    

    if (matcher.group(2) == null) {
      notes.add(root.major3());
      notes.add(root.five());
    } else {
      String type = matcher.group(2).toLowerCase();
      switch(type) {
      case "dim7":
      case "o7":
          notes.add(root.minor3());
          notes.add(root.flat5());
          notes.add(root.major6());
          break;
      case "dim":
      case "o":
        notes.add(root.minor3());
        notes.add(root.flat5());
        break;
      case "aug":
      case "+":
        notes.add(root.major3());
        notes.add(root.sharp5());
        break;
      case "sus4":
      case "sus":
        notes.add(root.four());
        notes.add(root.five());
        break;
      case "sus2":
        notes.add(root.nine());
        notes.add(root.five());
        break;
      case "maj7":
      case "Δ7":
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.sharp7());
        break;
      case "maj9":
      case "Δ9":
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.sharp7());
        notes.add(root.nine());
        break;
      case "mmaj7":
      case "mΔ7":
        notes.add(root.minor3());
        notes.add(root.five());
        notes.add(root.sharp7());
        break;
      case "maj":
        notes.add(root.major3());
        notes.add(root.five());
        break;
      case "m7":
      case "-7":
        notes.add(root.minor3());
        notes.add(root.five());
        notes.add(root.flat7());
        break;
      case "m":
      case "-":
        notes.add(root.minor3());
        notes.add(root.five());
        break;
      case "7":
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.flat7());
        break;
      case "7sus":
      case "7sus4":
        notes.add(root.four());
        notes.add(root.five());
        notes.add(root.flat7());
        break;
      case "7sus2":
        notes.add(root.nine());
        notes.add(root.five());
        notes.add(root.flat7());
        break;
      case "m6":
      case "-6":
        notes.add(root.minor3());
        notes.add(root.five());
        notes.add(root.major6());
        break;
      case "6":
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.major6());
        break;
      case "5":
        notes.add(root.five());
        break;
      }
    }

    String options = matcher.group(3);
    int matchedLength = 0;
    matcher = optionsPattern.matcher(options);
    while (matcher.find()) {
      matchedLength = matcher.end();
      final int degree = Integer.parseInt(matcher.group(2));
      Note option = degree == 7 ? Note.C.flat7() : CMajor.getNote(degree - 1);
      notes.remove(option.transpose(root.ordinal()));
      if ("#".equals(matcher.group(1))) {
        option = option.transpose(1);
        if (degree == 11) {
          notes.remove(root.five()); // #11 == b5
        }
      }
      else if ("b".equalsIgnoreCase(matcher.group(1))) {
        option = option.transpose(-1);
        if (degree == 13) {
          notes.remove(root.five()); // b13 == #5
        }
      }
      else if (Set.of(9, 11, 13).contains(degree)) {
        notes.add(root.flat7());
      }
      notes.add(option.transpose(root.ordinal()));
    }
    if (matchedLength != options.length()) {
      throw new ParseChordException("Invalid options in chord: " + string);
    }

    return new Scale(root, notes);
  }

  public String asChord(Scale chord) {
    if (asTriad(chord) != null)
        return asTriad(chord);
    
    StringBuilder sb = new StringBuilder();
    Set<Note> remaining = new TreeSet<Note>(chord.asSet());
    Note root = chord.getRoot();
    sb.append(root.getName(accidental));
    remaining.remove(root);
    
    boolean flat7 = false;
    
    // 3rd
    if (remaining.remove(root.major3())) {
      ; // implied
    } else if (remaining.remove(root.minor3())) {
      if (remaining.contains(root.flat5()) && remaining.contains(root.major6())) {
        remaining.remove(root.flat5());
        remaining.remove(root.major6());
        sb.append("dim7");
      } else if (remaining.contains(root.flat5()) && remaining.size() == 1) {
        remaining.remove(root.flat5());
        sb.append("dim");
      } else
        sb.append("m");
    } else if (remaining.remove(root.four())) {
      if (remaining.remove(root.flat7())) {
        flat7 = true;
        sb.append("7");
      }
      sb.append("sus4");
    }
    else if (remaining.remove(root.nine())) {
      if (remaining.remove(root.flat7())) {
        flat7 = true;
        sb.append("7");
      }
      sb.append("sus2");
    }
    else if (remaining.remove(root.five()))
      sb.append("5");

    // 7th
    if (remaining.remove(root.flat7())) {
      if (!remaining.contains(root.nine()) && !remaining.contains(root.four()) && !remaining.contains(root.major6())) {
        sb.append("7");
      }
      flat7 = true;
    }
    if (remaining.remove(root.sharp7())) {
      if (remaining.remove(root.nine())) {
        sb.append("Δ9");
      } else {
        sb.append("Δ7");
      }
    }

    // options
    if (remaining.remove(root.flat5()))
      sb.append("b5");
    if (remaining.remove(root.sharp5()))
      sb.append("#5");
    if (!flat7 && remaining.remove(root.major6()))
      sb.append("6");
    if (remaining.remove(root.flat9()))
      sb.append("b9");
    if (remaining.remove(root.nine()))
      sb.append("9");
    if (remaining.remove(root.minor3()))
      sb.append("#9");
    if (remaining.remove(root.four()))
      sb.append("11");
    if (flat7 && remaining.remove(root.major6()))
      sb.append("13");

    return sb.toString().trim();
  }
  
  @SuppressWarnings("serial")
  static class NoteSet extends TreeSet<Note> {
    public NoteSet(Scale scale) {
      super(scale.asSet());
    }
    boolean containsAll(Note ...notes) {
      return super.containsAll(Sets.newTreeSet(Arrays.asList(notes)));
    }
  }

  private String asTriad(Scale chord) {
    if (chord.asSet().size() != 3)
      return null;
    String root = chord.getRoot().getName(accidental);
    NoteSet set = new NoteSet(chord.transpose(C));
    if (set.containsAll(C, E, G))
      return root;
    if (set.containsAll(C, Eb, G))
      return root + "m";
    if (set.containsAll(C, Eb, Gb))
      return root + "dim";
    if (set.containsAll(C, E, Ab))
      return root + "aug";
    if (set.containsAll(C, D, G))
      return root + "sus2";
    if (set.containsAll(C, F, G))
      return root + "sus4";
    return null;
  }

}
