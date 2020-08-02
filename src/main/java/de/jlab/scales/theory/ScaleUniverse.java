package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.*;
import static java.util.stream.Collectors.toList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import de.jlab.scales.theory.ScaleUniverse.Namer.NamerBuilder;
import lombok.Builder;

public class ScaleUniverse implements Iterable<Scale> {

  private boolean includeModes;

  @lombok.Builder
  static class Namer {
    // {0} mode root name
    // {1} mode||scale name
    // {2} parent root name
    private final String namePattern;
    private final String nameFallbackPattern;
    private final String scaleName;
    private final String[] modeNames;
    private final Accidental accidental;

    public String name(int index, Note oldRoot, Note newRoot) {
      String oldRootName = oldRoot.getName(accidental);
      String newRootName = newRoot.getName(accidental);
      String modeName = modeName(index);
      if (modeName != null) {
        return MessageFormat.format(namePattern, newRootName, modeName, oldRootName);
      }
      return MessageFormat.format(nameFallbackPattern, newRootName, scaleName, oldRootName);
    }

    public String typeName(int index) {
      return modeName(index) == null ? scaleName : modeName(index);
    }

    private String modeName(int index) {
      if (index == 0) {
        return scaleName;
      }
      if (index < modeNames.length) {
        return modeNames[index];
      }
      return null;
    }
  }

  public ScaleUniverse() {
    this(false);
  }

  public ScaleUniverse(boolean includeModes) {
    this(includeModes, BuiltInScaleTypes.values());
  }

  public ScaleUniverse(ScaleType... types) {
    this(false, types);
  }

  public ScaleUniverse(boolean includeModes, ScaleType... types) {
    this.includeModes = includeModes;
    Stream.of(types).forEachOrdered(this::add);
    initializeSubScales();
  }

  private void add(ScaleType type) {
    if (isChord(type.getPrototype())) {
      chord(type);
    } else {
      scale(type);
    }
  }

  // TODO: move to scaleType?
  private boolean isChord(Scale scale) {
    return scale.length() < 5;
  }

  private ListMultimap<Scale, ScaleInfo> infos = MultimapBuilder.hashKeys().arrayListValues().build();
  private List<Scale> scales = new ArrayList<>();

  private void scale(ScaleType type) {
    NamerBuilder namerBuilder = Namer.builder().namePattern("{0} {1}").nameFallbackPattern("{2} {1}/{0}").scaleName(type.getScaleName()).modeNames(type.getModeNames());
    addAll(type, namerBuilder);
  }

  private void chord(ScaleType type) {
    NamerBuilder namerBuilder = Namer.builder().namePattern("{0}{1}").nameFallbackPattern("{2}{1}/{0}").scaleName(type.getScaleName()).modeNames(type.getModeNames());
    addAll(type, namerBuilder);
  }

  private void addAll(ScaleType type, NamerBuilder namerBuilder) {
    Scale scale = type.getPrototype();
    for (Note newRoot : Note.values()) {
      Scale transposed = scale.transpose(newRoot);
      addModes(transposed, namerBuilder.accidental(Accidental.fromScale(transposed)).build());
    }
  }

  private void addModes(Scale parent, ScaleUniverse.Namer namer) {
    Note oldRoot = parent.getRoot();
    Accidental accidental = Accidental.fromScale(parent);
    int numberOfModes = this.includeModes ? parent.length() : 1;
    for (int i = 0; i < numberOfModes; i++) {
      Note newRoot = parent.getNote(i);
      Scale mode = parent.superimpose(newRoot);
      ScaleInfo info = ScaleInfo.builder().accidental(accidental).scale(mode).parent(parent).typeName(namer.typeName(i)).name(namer.name(i, oldRoot, newRoot)).build();
      infos.put(mode, info);
      scales.add(mode);
    }
  }

  private final Comparator<ScaleInfo> infoQuality = (a, b) -> {
    return a.isInversion() == b.isInversion() ? 0 : (a.isInversion() ? 1 : -1);
  };

  public List<ScaleInfo> infos(Scale scale) {
    return infos.get(scale).stream().sorted(infoQuality).collect(toList());
  }

  public ScaleInfo info(Scale scale) {
    return infos(scale).stream().findFirst().orElseGet(() -> defaultInfo(scale));
  }

  private ScaleInfo defaultInfo(Scale scale) {
    ScaleInfo info = ScaleInfo.builder().scale(scale).typeName(scale.asIntervals()).parent(scale).build();
    initializeDefaultInfoSubScales(info);
    Accidental accidental = Accidental.fromScale(info.getSuperScales().stream().findFirst().orElse(scale));
    info.setAccidental(accidental);
    info.setName(isChord(scale) ? scale.asChord(accidental) : scale.asScale(accidental));
    return info;
  }

  private void initializeDefaultInfoSubScales(ScaleInfo info) {
    Set<? extends Note> notes = info.getScale().getNotes();
    for (Scale scale : this) {
      if (scale.getNotes().equals(notes)) {
        continue;
      }
      if (scale.getNotes().containsAll(notes)) {
        info.getSuperScales().add(scale);
      }
      if (notes.containsAll(scale.getNotes())) {
        info.getSubScales().add(scale);
      }
    }
  }

  private void initializeSubScales() {
    for (Scale superScale : this) {
      for (Scale subScale : this) {
        Set<? extends Note> superSet = superScale.getNotes();
        Set<? extends Note> subSet = subScale.getNotes();
        if (superSet.equals(subSet))
          continue;
        if (superSet.containsAll(subSet)) {
          infos.get(subScale).forEach(info -> info.getSuperScales().add(superScale));
          infos.get(superScale).forEach(info -> info.getSubScales().add(subScale));
        }
      }
    }
  }

  @Override
  public Iterator<Scale> iterator() {
    return scales.iterator();
  }

}
