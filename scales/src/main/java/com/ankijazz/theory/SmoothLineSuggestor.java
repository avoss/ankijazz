package com.ankijazz.theory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * if chords/scales change quickly, you want to play lines where every note belongs to
 * a single chord/scale and the notes are closely together.
 */
public class SmoothLineSuggestor {
  public enum Mode {
    STRICT_DESCENDING {
      @Override
      Note nearest(Note initial, Note note, Scale nextChord) {
        return DESCENDING.nearest(initial, note.transpose(-1), nextChord);
      }
    },
    DESCENDING {
      @Override
      Note nearest(Note initial, Note note, Scale nextChord) {
        while (!nextChord.contains(note)) {
          note = note.transpose(-1);
        }
        return note;
      }
    },
    STEADY {
      @Override
      Note nearest(Note initial, Note note, Scale nextChord) {
        Note ascending = ASCENDING.nearest(initial,  note, nextChord);
        Note descending = DESCENDING.nearest(initial,  note, nextChord);
        return (ascending.distance(initial) < descending.distance(initial)) ? ascending : descending;
      }
    },
    ASCENDING {
      @Override
      Note nearest(Note initial, Note note, Scale nextChord) {
        while (!nextChord.contains(note)) {
          note = note.transpose(1);
        }
        return note;
      }
    },
    STRICT_ASCENDING {
      @Override
      Note nearest(Note initial, Note note, Scale nextChord) {
        return ASCENDING.nearest(initial, note.transpose(1), nextChord);
      }
    };

    abstract Note nearest(Note initial, Note current, Scale nextChord);
  }

  public static class Line implements Comparable<Line> {
    private final Mode mode;
    private final int numberOfSemitones;
    private final List<Note> notes;

    public Line(Mode mode, List<Note> notes) {
      this.mode = mode;
      this.notes = notes;
      Note first = notes.get(0);
      this.numberOfSemitones = notes.stream().mapToInt(note -> first.distance(note)).max().getAsInt();
    }

    public static Line of(Mode mode, List<Note> line) {
      return new Line(mode, line);
    }

    public Mode getMode() {
      return mode;
    }

    public int getNumberOfSemitones() {
      return numberOfSemitones;
    }

    public List<Note> getNotes() {
      return notes;
    }

    @Override
    public String toString() {
      return "Line [mode=" + mode + ", numberOfSemitones=" + numberOfSemitones + ", notes=" + notes + "]";
    }

    @Override
    public int compareTo(Line that) {
      return Integer.compare(this.numberOfSemitones, that.numberOfSemitones);
    }
  }

  public List<Line> suggest(Mode mode, List<Scale> chords) {
    Scale firstChord = chords.get(0);
    return firstChord.stream().map(root -> findLine(root, mode, chords)).sorted().collect(Collectors.toList());
  }

  private Line findLine(Note initial, Mode mode, List<Scale> chords) {
    Note current = initial;
    List<Note> notes = new ArrayList<Note>();
    notes.add(current);
    for (int i = 1; i < chords.size(); i++) {
      Note next = mode.nearest(initial, current, chords.get(i));
      notes.add(next);
      current = next;
    }
    return new Line(mode, notes);

  }
}
