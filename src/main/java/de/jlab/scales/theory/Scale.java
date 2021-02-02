package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Scale implements Iterable<Note>, Comparable<Scale> {

  private final Note root;
  private final Set<Note> notes = new TreeSet<Note>();

  public Scale(Note root, Note... notes) {
    this(root, Arrays.asList(notes));
  }

  public Scale(Note root, Collection<Note> notes) {
    this.root = root;
    internalAdd(root);
    for (Note note : notes)
      internalAdd(note);
  }

  public Scale(Scale other) {
    this.root = other.root;
    this.notes.addAll(other.notes);
  }

  public Scale transpose(int semitones) {
    if (semitones == 0) {
      return this;
    }
    Scale scale = new Scale(root.transpose(semitones));
    for (Note note : notes)
      scale.internalAdd(note.transpose(semitones));
    return scale;
  }

  public Scale transpose(Note newRoot) {
    return transpose(newRoot.ordinal() - root.ordinal());
  }

  public Scale superimpose(int semitones) {
    return superimpose(root.transpose(semitones));
  }
  
  public Scale superimpose(Note newRoot) {
    if (newRoot == this.root) {
      return this;
    }
    Scale scale = new Scale(newRoot);
    scale.notes.addAll(notes);
    return scale;
  }

  public List<Scale> getInversions() {
    List<Scale> result = new ArrayList<Scale>();
    for (int i = 0; i < notes.size(); i++) {
      Note note = getNote(i);
      result.add(superimpose(note));
    }
    return result;
  }

  public List<Scale> getChords(int numberOfNotes) {
    List<Scale> result = new ArrayList<Scale>();
    for (int i = 0; i < notes.size(); i++)
      result.add(getChord(i, numberOfNotes));
    return result;
  }

  public Scale getChord(int degree) {
    return getChord(degree, 4);
  }

  public Scale getChord(int degree, int numberOfNotes) {
    Scale scale = new Scale(getNote(degree));
    for (int i = 0; i < numberOfNotes; i++) {
      scale.internalAdd(getNote(degree));
      degree += 2;
    }
    return scale;
  }

  public boolean contains(Note note) {
    return notes.contains(note);
  }

  public String asChord() {
    return new ChordParser().asChord(this);
  }

  public String asChord(Accidental accidental) {
    return new ChordParser().asChord(this, accidental);
  }

  private void internalAdd(Note note) {
    notes.add(note);
  }

  private void internalRemove(Note note) {
    if (note == root)
      throw new IllegalArgumentException("can not remove root from chord/scale");
    notes.remove(note);
  }

  public Scale add(Note note) {
    Scale result = new Scale(this);
    result.internalAdd(note);
    return result;
  }

  public Scale remove(Note note) {
    Scale result = new Scale(this);
    result.internalRemove(note);
    return result;
  }

  public List<Note> asList() {
    List<Note> list = new ArrayList<>();
    for (int i = 0; i < getNumberOfNotes(); i++) {
      list.add(this.getNote(i));
    }
    return list;
  }
  
  public Stream<Note> stream() {
    return asList().stream();
  }
  
  public Iterator<Note> iterator() {
    return new Iterator<Note>() {
      int index = 0;
      Note note = root; 

      @Override
      public boolean hasNext() {
        return index < notes.size();
      }

      @Override
      public Note next() {
        Note value = note;
        do {
          note = note.transpose(1);
        } while (!contains(note));
        index++;
        return value;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public Note getNote(int degree) {
    degree = modSize(degree);
    Iterator<Note> iter = iterator();
    for (int i = 0; i < degree; i++)
      iter.next();
    return iter.next();
  }

  public int indexOf(Note note) {
    int index = 0;
    for (Iterator<Note> iter = iterator(); iter.hasNext();) {
      if (note == iter.next())
        return index;
      index++;
    }
    return -1;
  }

  @Override
  public String toString() {
    return asScale(FLAT);
  }
  
  public String asScale(Accidental accidental) {
    return stream().map(n -> n.getName(accidental)).collect(joining(" "));
  }

  public String asIntervals() {
    return asIntervals(root);
  }

  public String asIntervals(Note root) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < notes.size(); i++) {
      sb.append(root.intervalName(getNote(i))).append(" ");
    }
    return sb.toString().trim();

  }

  @Override
  public int hashCode() {
    return notes.hashCode() + root.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Scale))
      return false;
    Scale that = (Scale) object;
    return this.root == that.root && this.notes.equals(that.notes);
  }

  public Set<Note> asSet() {
    return new TreeSet<>(notes);
  }

  public final Note getRoot() {
    return root;
  }

  @Override
  public int compareTo(Scale that) {
    Iterator<Note> it = that.iterator();
    for (Note n1 : this) {
      Note n2 = it.next();
      if (n1 != n2)
        return n1.compareTo(n2);
    }
    return 0;
  }

  public List<Integer> intervals() {
    List<Integer> intervals = new ArrayList<Integer>();
    Iterator<Note> it = iterator();
    Note prevNote = it.next();
    while (it.hasNext()) {
      Note nextNote = it.next();
      intervals.add(prevNote.semitones(nextNote));
      prevNote = nextNote;
    }
    intervals.add(prevNote.semitones(root));
    return intervals;
  }

  public boolean isSus4() {
    return !isMajor() && !isMinor() && contains(root.four());
  }
  
  public boolean isMajor() {
    return contains(root.major3());
  }

  public boolean isMinor() {
    return contains(root.minor3()) && !contains(root.major3());
  }
  
  public boolean isDominant() {
    return isMajor() && contains(root.minor7());
  }
  

  public boolean isAlteredDominant() {
    if (!isDominant())
      return false;
    return contains(root.flat5()) || contains(root.sharp5()) || contains(root.flat9()) || contains(root.sharp9());
  }
  
  public boolean isInversionOf(Scale other) {
    return asSet().equals(other.asSet());
  }

  public Set<Note> commonNotes(Scale other) {
    Set<Note> commons = new TreeSet<Note>(notes);
    commons.retainAll(other.notes);
    return commons;
  }

  private int modSize(int index) {
    while (index < 0)
      index += (notes.size() * 1000);
    return index % notes.size();
  }

  public int getNumberOfNotes() {
    return notes.size();
  }

  public boolean contains(Scale chord) {
    return this.notes.containsAll(chord.asSet());
  }

  public List<Note> stackedThirds() {
    return new Stacker(this).getStackedThirds();
  }
  
}
