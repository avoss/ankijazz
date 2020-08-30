package de.jlab.scales.theory;

public enum Accidental {
  FLAT() {

    @Override
    public Note apply(Note note) {
      return note.transpose(-1);
    }

    @Override
    public Accidental inverse() {
      return SHARP;
    }

    @Override
    public String symbol() {
      return "b";
    }

    @Override
    public Accidental twice() {
      return DOUBLE_FLAT;
    }

  },
  SHARP {
    @Override
    public Note apply(Note note) {
      return note.transpose(1);
    }

    @Override
    public Accidental inverse() {
      return FLAT;
    }

    @Override
    public String symbol() {
      return "#";
    }

    @Override
    public Accidental twice() {
      return DOUBLE_SHARP;
    }
  
  }, 
  DOUBLE_FLAT {

    @Override
    public Note apply(Note note) {
      return note.transpose(-2);
    }

    @Override
    public String symbol() {
      return "bb";
    }

    @Override
    public Accidental inverse() {
      return DOUBLE_SHARP;
    }

    @Override
    public Accidental twice() {
      throw new IllegalStateException("double flat cannot be applied twice");
    }
    
  },
  DOUBLE_SHARP {

    @Override
    public Note apply(Note note) {
      return note.transpose(2);
    }

    @Override
    public String symbol() {
      return "x";
    }

    @Override
    public Accidental inverse() {
      return DOUBLE_FLAT;
    }
    @Override
    public Accidental twice() {
      throw new IllegalStateException("double sharp cannot be applied twice");
    }
    
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Accidental inverse();
  
  public abstract Accidental twice();
  
}
