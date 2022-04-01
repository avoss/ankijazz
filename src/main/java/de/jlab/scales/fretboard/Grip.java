package de.jlab.scales.fretboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.jlab.scales.theory.Note;

public class Grip {

  
  /**
   * displays inversions of given chord
   */
  public static class FrettedNote {
    private final Note string;
    private final int fretNumber;
    private final char symbol;

    public FrettedNote(Note string, int fretNumber, char symbol) {
      this.string = string;
      this.fretNumber = fretNumber;
      this.symbol = symbol;
    }

    public Note getString() {
      return string;
    }

    public int getFretNumber() {
      return fretNumber;
    }

    public char getSymbol() {
      return symbol;
    }

    public Note getNote() {
      return string.transpose(fretNumber);
    }

    public FrettedNote shift(int numberOfFrets, char newSymbol) {
      return new FrettedNote(string, fretNumber + numberOfFrets, newSymbol);
    }

    @Override
    public String toString() {
      return "FrettedNote [string=" + string + ", fretNumber=" + fretNumber + ", symbol=" + symbol + "]\n";
    }

    public String toString(int minFret, int maxFret) {
      return "|" + IntStream.rangeClosed(minFret, maxFret).mapToObj(i -> (i == fretNumber ? "-" + symbol + "-|" : "---|")).collect(Collectors.joining());
    }

  }

  public static class GripBuilder {
    private List<FrettedNote> frettedNotes = new ArrayList<>();

    public GripBuilder fret(Note string, int fretNumber, char symbol) {
      frettedNotes.add(new FrettedNote(string, fretNumber, symbol));
      return this;
    }
    
    public Grip build() {
      return new Grip(frettedNotes);
    }
    
  }
  
  public static GripBuilder builder() {
    return new GripBuilder();
  }
  
  private final List<FrettedNote> frettedNotes;

  private Grip(List<FrettedNote> frettedNotes) {
    this.frettedNotes = frettedNotes;
  }

  public Grip nextInversion() {
    Map<Note, Character> map = createSymbolMap();
    List<FrettedNote> newNotes = new ArrayList<>();
    for (FrettedNote note : frettedNotes) {
      newNotes.add(findNextChordToneOnString(note, map));
    }
    return new Grip(newNotes);
  }

  private FrettedNote findNextChordToneOnString(FrettedNote note, Map<Note, Character> map) {
    for (int i = 1; i <= 12; i++) {
      Note newNote = note.getNote().transpose(i);
      if (map.containsKey(newNote)) {
        return new FrettedNote(note.getString(), note.getFretNumber() + i, map.get(newNote));
      }
    }
    throw new IllegalStateException("could find chord tone");
  }

  Map<Note, Character> createSymbolMap() {
    return frettedNotes.stream().collect(Collectors.toMap(n -> n.getNote(), n -> n.getSymbol()));
  }

  public List<FrettedNote> getFrettedNotes() {
    return frettedNotes;
  }

  @Override
  public String toString() {
    int firstFret = frettedNotes.stream().min((a, b) -> a.fretNumber - b.fretNumber).get().fretNumber - 1;
    int lastFret = frettedNotes.stream().max((a, b) -> a.fretNumber - b.fretNumber).get().fretNumber + 1; 
    return frettedNotes.stream().map(n -> n.toString(firstFret, lastFret)).collect(Collectors.joining("\n"));
  }

}
