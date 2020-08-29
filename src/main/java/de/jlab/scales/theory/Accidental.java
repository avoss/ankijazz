package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

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
  
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Accidental inverse();
  
}
