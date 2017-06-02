package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat5;
import static de.jlab.scales.theory.Scales.C7flat5flat9;
import static de.jlab.scales.theory.Scales.C7flat5sharp9;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.C7sharp5;
import static de.jlab.scales.theory.Scales.C7sharp5flat9;
import static de.jlab.scales.theory.Scales.C7sharp5sharp9;
import static de.jlab.scales.theory.Scales.C7sharp9;
import static de.jlab.scales.theory.Scales.C7sus4;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.Cdim7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * see vid @ 57:00 dominant 7 chord, unaltered (9,11,13) or altered, sus4.
 * Altered dominants can be replaced by unaltered ones and vice versa. 2-5-1 can
 * be played randomly in any keys major/minor 2-5-1 Semitone movement is also
 * strong (e.g. C#Maj7 -> CMaj7), - mII (or V7?) may be replaced with bIIMaj7
 * because all notes of bIIMaj7 are 1/2 step above root
 * 
 */
public class BackCycler {
  
  abstract class Leader {
    abstract Scale leadTo(Scale targetChord, Set<Note> melodyNotes);
  }
  
  class MyLeader extends Leader {
    Random random = new Random();
    List<Scale> chords = new ArrayList<Scale>();
    MyLeader() {
      add(C7);
      add(C7sus4);
//      add(Cm7b5.transpose(E));
//      add(Cm7.transpose(A));
      add(Cdim7.transpose(1));
      chords.add(C7sharp5);
      chords.add(C7flat5);
      chords.add(C7sharp9);
      chords.add(C7flat9);
      chords.add(C7sharp5sharp9);
      chords.add(C7sharp5flat9);
      chords.add(C7flat5sharp9);
      chords.add(C7flat5flat9);
      chords.addAll(CMelodicMinor.transpose(Db).getChords(4));
    }

    private void add(Scale chord) {
      chords.add(chord);
      chords.add(chord.transpose(C.tritone()));
    }

    @Override
    Scale leadTo(Scale targetChord, Set<Note> melodyNotes) {
      Note root = targetChord.getRoot().five();
      Chooser chooser = new Chooser(new Scale(root, melodyNotes));
      for (Scale chord : chords)
        chooser.add(chord.transpose(root.ordinal()));
      return chooser.choose();
    }

    
  }

//  // class HowardMorganLeader extends Leader {
//  // }
//
//  class TomQualeLeader extends Leader {
//
//    Random random = new Random();
//    @Override
//    Scale leadTo(Scale targetChord, Set<Note> melodyNotes) {
//      Note targetRoot = targetChord.getRoot();
//      
//      List<Scale> candidates = buildCandidates(melodyNotes, targetRoot);
//      return chooseCandidate(candidates, melodyNotes, targetChord);
//    }
//
//    private Scale chooseCandidate(List<Scale> candidates, Set<Note> melodyNotes, Scale targetChord) {
//      return candidates.get(random.nextInt(candidates.size()));
//    }
//
//    private List<Scale> buildCandidates(Set<Note> melodyNotes, Note targetRoot) {
//      List<Scale> candidates = new ArrayList<>();
//      
//      Scale mixolydianScale = CMajor.transpose(targetRoot.five());
//      if (mixolydianScale.getNotes().containsAll(melodyNotes)) 
//        candidates.add(createDominant7Chord(melodyNotes, mixolydianScale.getRoot()));
//      
//      Scale alteredScale = CMelodicMinor.transpose(targetRoot.sharp5());
//      if (alteredScale.getNotes().containsAll(melodyNotes)) 
//        candidates.add(createDominant7Chord(melodyNotes, targetRoot.flat9()));
//      System.out.println(candidates.size());
//      if (candidates.isEmpty()) {
//        candidates.add(new Scale(targetRoot.five(), melodyNotes));
//      }
//      return candidates;
//    }
//
//    protected Scale createDominant7Chord(Set<Note> melodyNotes, Note root) {
//      Scale chord = new Scale(root);
//      if (melodyNotes.contains(root.four()))
//        chord = chord.add(root.four());
//      else
//        chord = chord.add(root.major3());
//      chord = chord.add(root.flat7());
//      for (Note note : melodyNotes)
//        chord = chord.add(note);
//      return chord;
//    }
//
//  }
//
  private List<Set<Note>> melody = new ArrayList<>();
  private Scale targetChord;
  private Leader leader = new MyLeader();

  public BackCycler(Scale targetChord) {
    this.targetChord = targetChord;
  }

  public BackCycler add(Note... notes) {
    melody.add(new HashSet<Note>(Arrays.asList(notes)));
    return this;
  }

  public List<Scale> backcycle() {
    List<Scale> result = new ArrayList<>();
    result.add(targetChord);
    Scale chord = targetChord;
    for (Set<Note> notes : reverseMelody()) {
      chord = leader.leadTo(chord, notes);
      result.add(chord);
    }
    Collections.reverse(result);
    return result;
  }

  private List<Set<Note>> reverseMelody() {
    List<Set<Note>> reversedMelody = new ArrayList<>(melody);
    Collections.reverse(reversedMelody);
    return reversedMelody;
  }

  public void seq(Note ...notes) {
    for (Note note : notes)
      add(note);
  }
  
}
