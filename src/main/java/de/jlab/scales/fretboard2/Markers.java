package de.jlab.scales.fretboard2;

import java.util.function.Function;

import de.jlab.scales.fretboard2.MarkerRenderer.LeftRight;
import de.jlab.scales.fretboard2.MarkerRenderer.UpDown;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import lombok.Builder;
import lombok.Data;

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

  public static Marker empty() {
    return EMPTY;
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

  public static Function<Note, Marker> marker(Scale scale) {
    return marker(scale.getRoot(), scale);
  }

  public static Function<Note, Marker> marker(Note root, Scale foreground) {
    return n -> n == root ? Markers.root() : (foreground.contains(n) ? Markers.foreground() : Markers.background());
  }

  @Builder
  @Data
  public static class ArrowMarkerFunction implements Function<Note, Marker> {
    private final Position position;
    private final Note root;
    private final LeftRight leftRight;
    private final UpDown upDown;

    private ArrowMarkerFunction(Position position, Note root, LeftRight leftRight, UpDown upDown) {
      this.position = position;
      this.root = root;
      this.leftRight = leftRight;
      this.upDown = upDown;
    }

    @Override
    public Marker apply(Note note) {
      // TODO Auto-generated method stub
      return null;
    }
  }

}
