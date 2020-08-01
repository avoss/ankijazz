package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.BuiltInScaleTypes.*;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

public class ScalePrinter {

  private Accidental defaultAccidental = Accidental.SHARP;
  private static ScaleUniverse universe = new ScaleUniverse(Major, MelodicMinor, HarmonicMinor);

  public String asScale(Scale scale) {
    Scale superScale = universe.info(scale).getSuperScales().stream().findFirst().orElse(scale);
    Optional<Accidental> optional = tryAccidental(superScale);
    if (optional.isPresent()) {
      return fromNaturals(scale, optional.get());
    }
    return fromScale(scale, defaultAccidental);
  }

  private String fromNaturals(Scale scale, Accidental accidental) {
    Set<Note> naturals = Sets.newHashSet(Note.NATURALS);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < scale.length(); i++) {
      Note note = scale.getNote(i);
      if (naturals.contains(note)) {
        sb.append(note.name()).append(" ");
      } else if (naturals.contains(accidental.remove(note))) {
        note = accidental.remove(note);
        sb.append(note.name()).append(accidental.symbol()).append(" ");
      } else {
        sb.append(scale.getNote(i).getName(accidental)).append(" ");
      }
      naturals.remove(note);
    }
    return sb.toString().trim();
  }

  private String fromScale(Scale scale, Accidental accidental) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < scale.length(); i++) {
      sb.append(scale.getNote(i).getName(accidental)).append(" ");
    }
    return sb.toString().trim();
  }

  Optional<Accidental> tryAccidental(Scale s) {
    if (tryAccidental(s, SHARP)) {
      return Optional.of(SHARP);
    }
    if (tryAccidental(s, FLAT)) {
      return Optional.of(FLAT);
    }
    return Optional.empty();
  }

  private boolean tryAccidental(Scale s, Accidental acc) {
    Set<Note> notes = s.getNotes();
    for (Note note : NATURALS) {
      if (!notes.remove(note)) {
        notes.remove(acc.apply(note));
      }
    }
    return notes.isEmpty();
  }

}
