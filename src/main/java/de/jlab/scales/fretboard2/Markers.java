package de.jlab.scales.fretboard2;


import java.util.function.Function;

import de.jlab.scales.fretboard2.BoxMarker.BoxPosition;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Markers {
  public static final Marker EMPTY = new Marker() {

    @Override
    public void render(MarkerRenderer renderer, GuitarString string, int fret) {
      renderer.renderEmpty(string, fret);
    }

    public boolean isEmpty() {
      return true;
    }

    @Override
    public String toString() {
      return "Empty";
    }
  };

  private Markers() {
  }

  public static Marker empty() {
    return EMPTY;
  }
  
  public static Marker foreground() {
    return new Marker() {

      @Override
      public void render(MarkerRenderer renderer, GuitarString string, int fret) {
        renderer.renderForeground(string, fret);
      }

      @Override
      public String toString() {
        return "Foreground";
      }
    };
  }

  public static Marker background() {
    return new Marker() {

      @Override
      public void render(MarkerRenderer renderer, GuitarString string, int fret) {
        renderer.renderBackground(string, fret);
      }

      @Override
      public String toString() {
        return "Background";
      }
    };
  }

  public static Marker root() {
    return new Marker() {

      @Override
      public void render(MarkerRenderer renderer, GuitarString string, int fret) {
        renderer.renderRoot(string, fret);
      }

      @Override
      public String toString() {
        return "Root";
      }
    };
  }
  
  public static Position box(Fretboard fretboard, int string, Note root, BoxPosition boxPosition, Fingering fingering) {
    BoxMarker marker = new BoxMarker(fretboard, string, root, boxPosition, fingering);
    return marker.mark();
  }

  public static Function<Note, Marker> marker(Scale foreground) {
    return marker(foreground.getRoot(), foreground);
  }

  public static Function<Note, Marker> marker(Note root, Scale foreground) {
    return n -> n == root ? Markers.root() : (foreground.contains(n) ? Markers.foreground() : Markers.background());
  }


}
