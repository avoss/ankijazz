package de.jlab.scales.fretboard2;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.jlab.scales.theory.Note;

public class BoxMarker implements Marker {
  public enum LeftRight { LEFT, RIGHT }
  
  private final Fretboard fretboard;
  private final int stringIndex;
  private final Note root;
  private final LeftRight boxPosition;
  private final Fingering fingering;
  
  private final int rootFret;
  private final Position position;
  private final GuitarString string;

  public BoxMarker(Fretboard fretboard, int stringIndex, Note root, LeftRight boxPosition, Fingering fingering) {
    this.fretboard = fretboard;
    this.stringIndex = stringIndex;
    this.root = root;
    this.boxPosition = boxPosition;
    this.fingering = fingering;
    
    string = fretboard.getString(stringIndex);
    int rootFret = string.fretOf(root);
    Optional<Position> position = findPosition(rootFret);
    if (position.isEmpty()) {
      rootFret += 12;
      position = findPosition(rootFret);
    }
    this.rootFret = rootFret;
    this.position = position.get();
  }
  
  Optional<Position> findPosition(int rootFret) {
    Comparator<Position> byFret = (a, b) -> Integer.compare(a.getMinFret(), b.getMinFret());
    Stream<Position> positions = fingering.getPositions().stream();
    if (boxPosition == LeftRight.LEFT) {
      Predicate<Position> filter = p -> rootFret > (p.getMinFret() + p.getMaxFret()) / 2.0;
      return positions.filter(filter).max(byFret);
    }
    Predicate<Position> filter = p -> rootFret < (p.getMinFret() + p.getMaxFret()) / 2.0;
    return positions.filter(filter).min(byFret);
  }
  
  public Position mark() {
    string.mark(rootFret, this);
    return position;
  }
  
  @Override
  public void render(MarkerRenderer renderer, GuitarString string, int fret) {
    renderer.renderBox(string, fret, minFret(), maxFret());
  }

  private int minFret() {
    return Math.min(fretboard.getMinFret(), position.getMinFret());
  }
  
  private int maxFret() {
    return Math.max(fretboard.getMaxFret(), position.getMaxFret());
  }

}