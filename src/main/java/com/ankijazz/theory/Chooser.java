package com.ankijazz.theory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Chooser {
  private Scale target;
  private Note root;
  private List<Entry> entries = new ArrayList<Entry>();
  private Random random = new Random();
  private static final int NOT_ACCEPTABLE = 100;
  private static final int ACCEPTABLE = 1;

  class Entry implements Comparable<Entry> {
    private int badness = 0;
    private Scale chord;
    
    Entry(Scale chord) {
      this.chord = chord;

      Set<Note> diff = new TreeSet<Note>(target.asSet());
      diff.removeAll(chord.asSet());
      
      for (Note unmatched : diff) {
        exclusive(unmatched, root.major3(), root.minor3());
        avoid(unmatched == root.major3(), root.four());
        altered(unmatched, root.five());
        altered(unmatched, root.nine());
        exclusive(unmatched, root.major6(), root.minor6());
        accept(true);
      }
    }
    
    private void exclusive(Note unmatched, Note major, Note minor) {
      accept(unmatched == major || unmatched == minor);
      avoid(unmatched == major, minor);
      avoid(unmatched == minor, major);
    }

    private void altered(Note unmatched, Note extension) {
      Note sharpenedExtension = extension.transpose(1);
      Note flattenedExtension = extension.transpose(-1);
      accept(unmatched == extension);
      avoid(unmatched == extension, sharpenedExtension, flattenedExtension);
      accept(unmatched == sharpenedExtension);
      avoid(unmatched == sharpenedExtension, extension);
      accept(unmatched == flattenedExtension);
      avoid(unmatched == flattenedExtension, extension);
    }
    

    private void accept(boolean match, Note ... dissonants) {
      weigh(ACCEPTABLE, match, dissonants);
    }

    private void avoid(boolean match, Note ... dissonants) {
      weigh(NOT_ACCEPTABLE, match, dissonants);
    }
    
    private void weigh(int points, boolean match, Note ... dissonants) {
      if (!match)
        return;
      boolean dissonant = dissonants.length == 0;
      for (Note note : dissonants) {
        if (chord.contains(note))
          dissonant = true;
      }
      if (dissonant)
        badness += points;
    }

    @Override
    public int compareTo(Entry that) {
      return Integer.compare(this.badness, that.badness);
    }
    
    @Override
    public String toString() {
      return chord.asChord() + " " + badness;
    }

    public int getBadness() {
      return badness;
    }

    public Scale getChord() {
      return chord;
    }
  }
  
  Chooser(Scale target) {
    this.target = target;
    this.root = target.getRoot();
  }
  
  Chooser add(Scale chord) {
    entries.add(new Entry(chord));
    return this;
  }
  
  Scale choose() {
    Collections.sort(entries);
    //System.out.println(entries);
    int best = entries.get(0).getBadness();
    List<Scale> list = new ArrayList<>();
    for (Entry e : entries) {
      if (e.getBadness() == best /* || e.getBadness() < NOT_ACCEPTABLE */)
        list.add(e.getChord());
    }
    return list.get(random.nextInt(list.size()));
  }
  
}