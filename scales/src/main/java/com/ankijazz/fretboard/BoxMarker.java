package com.ankijazz.fretboard;

import static java.lang.Math.abs;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.ankijazz.fretboard.Fretboard.Box;
import com.ankijazz.theory.Note;

public class BoxMarker {
  public enum BoxPosition { LEFT, RIGHT }
  
  private final Fretboard fretboard;
  private final BoxPosition boxPosition;
  private final Fingering fingering;
  
  private final int rootFret;
  private final Position position;
  private final GuitarString string;
  private Marker marker;
  private int stringIndex;

    
  public BoxMarker(Fretboard fretboard, int stringIndex, Note root, BoxPosition boxPosition, Fingering fingering) {
    this(fretboard, stringIndex, root, boxPosition, fingering, Marker.FOREGROUND);
  }
  
  public BoxMarker(Fretboard fretboard, int stringIndex, Note root, BoxPosition boxPosition, Fingering fingering, Marker marker) {
    this.fretboard = fretboard;
    this.stringIndex = stringIndex;
    this.boxPosition = boxPosition;
    this.fingering = fingering;
    this.marker = marker;
    this.string = fretboard.getString(stringIndex);
    
    int rootFret1 = string.fretOf(root);
    int rootFret2 = rootFret1 + 12;
    Optional<Position> position1 = findPosition(rootFret1);
    Optional<Position> position2 = findPosition(rootFret2);
    if (position1.isEmpty()) {
      this.rootFret = rootFret2;
      this.position = position2.get();
    } else if (position2.isEmpty()) {
      this.rootFret = rootFret1;
      this.position = position1.get();
    } else {
      Position p1 = position1.get();
      int badness1 = new PositionComparator(rootFret1).badness(p1);
      Position p2 = position2.get();
      int badness2 = new PositionComparator(rootFret2).badness(p2);
      if (badness1 < badness2) {
        this.rootFret = rootFret1;
        this.position = p1;
      } else {
        this.rootFret = rootFret2;
        this.position = p2;
      }
    }
  }

  class PositionComparator implements Comparator<Position> {

    final Function<Position, Integer> fretNumber;
    final int rootFret;
    
    PositionComparator(int rootFret) {
      this.rootFret = rootFret;
      this.fretNumber = boxPosition == BoxPosition.RIGHT 
          ? (p) -> p.getMinFret() 
          : (p) -> p.getMaxFret();
    }
    
    @Override
    public int compare(Position a, Position b) {
      return Integer.compare(badness(a), badness(b));
    }

    int badness(Position position) {
      int badness = abs(rootFret - fretNumber.apply(position));
      if (!position.getFrets(stringIndex).contains(rootFret)) {
        badness += 1000;
      }
      return badness;
    }
    
  }

  Optional<Position> findPosition(int rootFret) {
    PositionComparator byFret = new PositionComparator(rootFret);
    return fingering.getPositions().stream().min(byFret);
  }
  
  Optional<Position> findPositionWithoutRoot(int rootFret) {
    Comparator<Position> byFret = (a, b) -> Integer.compare(a.getMinFret(), b.getMinFret());
    Stream<Position> positions = fingering.getPositions().stream();
    if (boxPosition == BoxPosition.LEFT) {
      Predicate<Position> filter = p -> rootFret >= middleFret(p);
      return positions.filter(filter).max(byFret);
    }
    Predicate<Position> filter = p -> rootFret <= middleFret(p);
    return positions.filter(filter).min(byFret);
  }
  
  Optional<Position> findPositionContainingRoot(int rootFret) {
    Comparator<Position> byFret = (a, b) -> Integer.compare(a.getMinFret(), b.getMinFret());
    Predicate<Position> filter = p -> p.getFrets(stringIndex).contains(rootFret);
    Stream<Position> positions = fingering.getPositions().stream();
    if (boxPosition == BoxPosition.LEFT) {
      return positions.filter(filter).min(byFret);
    }
    return positions.filter(filter).max(byFret);
  }

  private double middleFret(Position p) {
    return (p.getMinFret() + p.getMaxFret()) / 2.0;
  }
  
  public Position mark() {
    string.mark(rootFret, marker);
    fretboard.setBox(new Box(minFret(), maxFret()));
    return position;
  }

  private int minFret() {
    return Math.min(fretboard.getMinFret(), position.getMinFret());
  }
  
  private int maxFret() {
    return Math.max(fretboard.getMaxFret(), position.getMaxFret());
  }

}