package de.jlab.scales.fretboard2;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.jlab.scales.fretboard2.Fretboard.Box;
import de.jlab.scales.theory.Note;

public class BoxMarker {
  public enum BoxPosition { LEFT, RIGHT }
  
  private final Fretboard fretboard;
  private final BoxPosition boxPosition;
  private final Fingering fingering;
  
  private final int rootFret;
  private final Position position;
  private final GuitarString string;
  private Marker marker;

    
  public BoxMarker(Fretboard fretboard, int stringIndex, Note root, BoxPosition boxPosition, Fingering fingering) {
    this(fretboard, stringIndex, root, boxPosition, fingering, Marker.FOREGROUND);
  }
  
  public BoxMarker(Fretboard fretboard, int stringIndex, Note root, BoxPosition boxPosition, Fingering fingering, Marker marker) {
    this.fretboard = fretboard;
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
      Position p2 = position2.get();
      double distance1 = Math.abs(rootFret1 - middleFret(p1));
      double distance2 = Math.abs(rootFret2 - middleFret(p2));
      if (distance1 < distance2) {
        this.rootFret = rootFret1;
        this.position = position1.get();
      } else {
        this.rootFret = rootFret2;
        this.position = position2.get();
      }
    }
  }
  
  Optional<Position> findPosition(int rootFret) {
    Comparator<Position> byFret = (a, b) -> Integer.compare(a.getMinFret(), b.getMinFret());
    Stream<Position> positions = fingering.getPositions().stream();
    if (boxPosition == BoxPosition.LEFT) {
      Predicate<Position> filter = p -> rootFret >= middleFret(p);
      return positions.filter(filter).max(byFret);
    }
    Predicate<Position> filter = p -> rootFret <= middleFret(p);
    return positions.filter(filter).min(byFret);
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