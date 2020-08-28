package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Accidental.*;
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

    public String name(int index, Note oldRoot, Note newRoot, Accidental accidental) {
      String oldRootName = oldRoot.getName(accidental);
      String newRootName = newRoot.getName(accidental);
      String modeName = internalModeName(index);
      if (modeName != null) {
        return MessageFormat.format(namePattern, newRootName, modeName, oldRootName);
      }
      if (index == 0) {
        return MessageFormat.format(namePattern, newRootName, scaleName, oldRootName);
      }
      return MessageFormat.format(nameFallbackPattern, newRootName, scaleName, oldRootName);
    }

    public String modeName(int index) {
      return internalModeName(index) == null ? scaleName : internalModeName(index);
    }

    private String internalModeName(int index) {
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
    Namer namer = Namer.builder()
        .namePattern("{0} {1}")
        .nameFallbackPattern("{2} {1}/{0}")
        .scaleName(type.getScaleName())
        .modeNames(type.getModeNames())
        .build();
    addAll(type, namer);
  }

  private void chord(ScaleType type) {
    Namer namer = Namer.builder()
        .namePattern("{0}{1}")
        .nameFallbackPattern("{2}{1}/{0}")
        .scaleName(type.getScaleName())
        .modeNames(type.getModeNames())
        .build();
    addAll(type, namer);
  }

  private void addAll(ScaleType type, Namer namer) {
    Scale scale = type.getPrototype();
    for (Note newRoot : Note.values()) {
      Scale transposed = scale.transpose(newRoot);
      addModes(transposed, namer);
    }
  }

  private void addModes(Scale parent, ScaleUniverse.Namer namer) {
    Note oldRoot = parent.getRoot();
    
    // in case of harmonic or melodic minor we need to take keysignature of corresponding natural minor.
    KeySignature keySignature = keySignatureFromParentScale(parent);
    
    int numberOfModes = this.includeModes ? parent.length() : 1;
    for (int i = 0; i < numberOfModes; i++) {
      Note newRoot = parent.getNote(i);
      Scale mode = parent.superimpose(newRoot);
      ScaleInfo info = ScaleInfo.builder()
          .keySignature(keySignature)
          .scale(mode)
          .parent(parent)
          .modeName(namer.modeName(i))
          .defaultName(namer.name(i, oldRoot, newRoot, Accidental.fromScale(parent)))
          .flatName(namer.name(i, oldRoot, newRoot, FLAT))
          .sharpName(namer.name(i, oldRoot, newRoot, SHARP))
          .build();
      infos.put(mode, info);
      scales.add(mode);
    }
  }

  private KeySignature keySignatureFromParentScale(Scale parent) {
    if (parent.isMinor()) {
      Scale major = CMajor.transpose(parent.getRoot()).transpose(3);
      return KeySignature.of(major.getRoot(), Accidental.fromScale(major));
    }
    return KeySignature.of(parent.getRoot(), Accidental.fromScale(parent));
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

  // FIXME throw / return null instead? - NO, because need to find scales for unknown chords
  private ScaleInfo defaultInfo(Scale scale) {
    ScaleInfo info = ScaleInfo.builder().scale(scale).modeName(scale.asIntervals()).parent(scale).build();
    initializeDefaultInfoSubScales(info);
    Scale superScale = info.getSuperScales().stream().findFirst().orElse(scale);
    
    // FIXME same here: if minor need to take key signature from natural minor
    Accidental accidental = Accidental.fromScale(superScale);
    info.setKeySignature(KeySignature.of(scale.getRoot(), accidental));
    
    info.setDefaultName(defaultName(scale, accidental));
    info.setFlatName(defaultName(scale, FLAT));
    info.setSharpName(defaultName(scale, SHARP));
    return info;
  }

  private String defaultName(Scale scale, Accidental accidental) {
    return isChord(scale) ? scale.asChord(accidental) : scale.asScale(accidental);
  }

  private void initializeDefaultInfoSubScales(ScaleInfo info) {
    Set<? extends Note> notes = info.getScale().asSet();
    for (Scale scale : this) {
      if (scale.asSet().equals(notes)) {
        continue;
      }
      if (scale.asSet().containsAll(notes)) {
        info.getSuperScales().add(scale);
      }
      if (notes.containsAll(scale.asSet())) {
        info.getSubScales().add(scale);
      }
    }
  }

  private void initializeSubScales() {
    for (Scale superScale : this) {
      for (Scale subScale : this) {
        Set<? extends Note> superSet = superScale.asSet();
        Set<? extends Note> subSet = subScale.asSet();
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
