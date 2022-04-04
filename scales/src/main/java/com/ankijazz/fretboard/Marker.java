package com.ankijazz.fretboard;


import java.util.function.Function;

import com.ankijazz.fretboard.BoxMarker.BoxPosition;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;

public enum Marker {
  EMPTY {
    @Override
    void render(MarkerRenderer renderer, GuitarString string, int fret) {
      renderer.renderEmpty(string, fret);
    }
  }, FOREGROUND {
    @Override
    void render(MarkerRenderer renderer, GuitarString string, int fret) {
      renderer.renderForeground(string, fret);
    }
  }, BACKGROUND {
    @Override
    void render(MarkerRenderer renderer, GuitarString string, int fret) {
      renderer.renderBackground(string, fret);
    }
  }, ROOT {
    @Override
    void render(MarkerRenderer renderer, GuitarString string, int fret) {
      renderer.renderRoot(string, fret);
    }
  };

  abstract void render(MarkerRenderer renderer, GuitarString string, int fret);
  
  public static Position box(Fretboard fretboard, int string, Note root, BoxPosition boxPosition, Fingering fingering) {
    return box(fretboard, string, root, boxPosition, fingering, ROOT);
  }
  
  public static Position box(Fretboard fretboard, int string, Note root, BoxPosition boxPosition, Fingering fingering, Marker marker) {
    return new BoxMarker(fretboard, string, root, boxPosition, fingering, marker).mark();
  }

  public static Function<Note, Marker> outline(Scale foreground) {
    return marker(foreground.getRoot(), foreground);
  }

  public static Function<Note, Marker> marker(Note root, Scale foreground) {
    return n -> n == root ? ROOT : (foreground.contains(n) ? FOREGROUND : BACKGROUND);
  }

}
