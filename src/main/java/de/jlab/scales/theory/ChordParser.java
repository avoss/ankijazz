package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

public class ChordParser {
  private static final Scale C_MAJOR = new Scale(C, D, E, F, G, A, B);
  private final Accidental accidental;

  private static final Pattern chordPattern = Pattern.compile("\\s*(C#|C|D#|Db|D|Eb|E|F#|F|G#|Gb|G|A#|Ab|A|Bb|B)\\s*(aug|dim7|dim|sus4|sus2|maj7|maj|m7|m|7|m6|6|5)?(.*)\\s*", Pattern.CASE_INSENSITIVE);
  private static final Pattern optionsPattern = Pattern.compile("(sus4)|(maj7)|((b|#)?(4|5|6|7|9|11|13))\\s*");
  
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
    Matcher matcher = chordPattern.matcher(string.toLowerCase());
    if (!matcher.find())
      throw new ParseChordException(string);
    Note root = noteNameMap.get(matcher.group(1).toLowerCase());
    if (root == null)
      throw new ParseChordException(string);
    Set<Note> notes = new TreeSet<Note>();
    

    if (matcher.group(2) == null) {
      notes.add(root.major3());
      notes.add(root.five());
    } else {
      String type = matcher.group(2).toLowerCase();
      if ("dim7".equals(type)) {
        notes.add(root.minor3());
        notes.add(root.flat5());
        notes.add(root.major6());
      } else if ("dim".equals(type)) {
        notes.add(root.minor3());
        notes.add(root.flat5());
      } else if ("aug".equals(type)) {
        notes.add(root.major3());
        notes.add(root.sharp5());
      } else if ("sus4".equals(type)) {
        notes.add(root.four());
        notes.add(root.five());
      } else if ("sus2".equals(type)) {
        notes.add(root.nine());
        notes.add(root.five());
      } else if ("maj7".equals(type)) {
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.sharp7());
      } else if ("maj".equals(type)) {
        notes.add(root.major3());
        notes.add(root.five());
      } else if ("m7".equals(type)) {
        notes.add(root.minor3());
        notes.add(root.five());
        notes.add(root.flat7());
      } else if ("m".equals(type)) {
        notes.add(root.minor3());
        notes.add(root.five());
      } else if ("7".equals(type)) {
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.flat7());
      } else if ("m6".equals(type)) {
        notes.add(root.minor3());
        notes.add(root.five());
        notes.add(root.major6());
      } else if ("6".equals(type)) {
        notes.add(root.major3());
        notes.add(root.five());
        notes.add(root.major6());
      } else if ("5".equals(type)) {
        notes.add(root.five());
      }
    }

    matcher = optionsPattern.matcher(matcher.group(3));
    while (matcher.find()) {
      if ("sus4".equals(matcher.group(1))) {
        notes.remove(root.major3());
        notes.remove(root.minor3());
        notes.add(root.four());
      } else if ("maj7".equals(matcher.group(2))) {
        notes.add(root.sharp7());
      } else {
        int degree = Integer.parseInt(matcher.group(5));
        Note option = degree == 7 ? Note.C.flat7() : C_MAJOR.getNote(degree - 1);
        notes.remove(option.transpose(root.ordinal()));
        if ("#".equals(matcher.group(4)))
          option = option.transpose(1);
        else if ("b".equalsIgnoreCase(matcher.group(4)))
          option = option.transpose(-1);
        notes.add(option.transpose(root.ordinal()));
      }
    }

    return new Scale(root, notes);
  }

  public String asChord(Scale chord) {
    if (asTriad(chord) != null)
        return asTriad(chord);
    
    StringBuilder sb = new StringBuilder();
    Set<Note> remaining = new TreeSet<Note>(chord.getNotes());
    Note root = chord.getRoot();
    sb.append(root.getName(accidental));
    remaining.remove(root);
    
    // 3rd
    if (remaining.remove(root.major3())) {
      ; // implied
    } else if (remaining.remove(root.minor3())) {
      if (remaining.contains(root.flat5()) && remaining.contains(root.major6())) {
        remaining.remove(root.flat5());
        remaining.remove(root.major6());
        sb.append("Dim7");
      } else if (remaining.contains(root.flat5()) && remaining.size() == 1) {
        remaining.remove(root.flat5());
        sb.append("Dim");
      } else
        sb.append("m");
    } else if (remaining.remove(root.four()))
      sb.append("Sus4");
    else if (remaining.remove(root.nine()))
      sb.append("Sus2");
    else if (remaining.remove(root.five()))
      sb.append("5");

    boolean flat7 = false;
    // 7th
    if (remaining.remove(root.flat7())) {
      sb.append("7");
      flat7 = true;
    }
    if (remaining.remove(root.sharp7()))
      sb.append("Maj7");

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
      super(scale.getNotes());
    }
    boolean containsAll(Note ...notes) {
      return super.containsAll(Sets.newTreeSet(Arrays.asList(notes)));
    }
  }

  private String asTriad(Scale chord) {
    if (chord.getNotes().size() != 3)
      return null;
    String root = chord.getRoot().getName(accidental);
    NoteSet set = new NoteSet(chord.transpose(C));
    if (set.containsAll(C, E, G))
      return root;
    if (set.containsAll(C, Eb, G))
      return root + "m";
    if (set.containsAll(C, Eb, Gb))
      return root + "Dim";
    if (set.containsAll(C, E, Ab))
      return root + "Aug";
    if (set.containsAll(C, D, G))
      return root + "Sus2";
    if (set.containsAll(C, F, G))
      return root + "Sus4";
    return null;
  }

}
