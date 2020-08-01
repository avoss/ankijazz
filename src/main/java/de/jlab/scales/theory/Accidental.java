package de.jlab.scales.theory;

public enum Accidental {
  FLAT() {

    @Override
    public Note apply(Note note) {
      return note.transpose(-1);
    }

    @Override
    public Note remove(Note note) {
      return note.transpose(1);
    }

    @Override
    public String symbol() {
      return "b";
    }

  },
  SHARP {
    @Override
    public Note apply(Note note) {
      return note.transpose(1);
    }

    @Override
    public Note remove(Note note) {
      return note.transpose(-1);
    }

    @Override
    public String symbol() {
      return "#";
    }
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Note remove(Note note);
}
