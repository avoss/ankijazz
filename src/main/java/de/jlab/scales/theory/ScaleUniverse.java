package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Note.*;
import static java.util.stream.Collectors.toList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

public class ScaleUniverse implements Iterable<Scale> {
  
  /**
   * TODO:
   * - add keysignature as separate pass, because superscales are required (chords, pentatonics)
   */


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

    public String typeName(int index) {
      return internalModeName(index) == null ? scaleName : internalModeName(index);
    }

    private String internalModeName(int index) {
      if (index < modeNames.length) {
        return modeNames[index];
      }
      return null;
    }
  }

  private ListMultimap<Scale, ScaleInfo> infos = MultimapBuilder.hashKeys().arrayListValues().build();
  private List<Scale> scales = new ArrayList<>();
  private boolean includeModes;

  public ScaleUniverse() {
    this(false);
  }

  public ScaleUniverse(boolean includeModes) {
    this(includeModes, BuiltInScaleTypes.values());
  }

  public ScaleUniverse(ScaleType... scaleTypes) {
    this(false, scaleTypes);
  }

  public ScaleUniverse(boolean includeModes, ScaleType... scaleTypes) {
    this.includeModes = includeModes;
    Stream.of(scaleTypes).forEachOrdered(this::add);
    initializeSubScales();
    // TODO: initializeKeySignatures() ????
  }

  private void add(ScaleType scaleType) {
    if (isChord(scaleType.getPrototype())) {
      chord(scaleType);
    } else {
      scale(scaleType);
    }
  }

  // TODO: move to scaleType?
  private boolean isChord(Scale scale) {
    return scale.length() < 5;
  }

  private void scale(ScaleType scaleType) {
    Namer namer = Namer.builder()
        .namePattern("{0} {1}")
        .nameFallbackPattern("{2} {1}/{0}")
        .scaleName(scaleType.getTypeName())
        .modeNames(scaleType.getModeNames())
        .build();
    addAll(scaleType, namer);
  }

  private void chord(ScaleType scaleType) {
    Namer namer = Namer.builder()
        .namePattern("{0}{1}")
        .nameFallbackPattern("{2}{1}/{0}")
        .scaleName(scaleType.getTypeName())
        .modeNames(scaleType.getModeNames())
        .build();
    addAll(scaleType, namer);
  }

  private void addAll(ScaleType scaleType, Namer namer) {
    Scale scale = scaleType.getPrototype();
    for (Note newRoot : Note.values()) {
      Scale transposed = scale.transpose(newRoot);
      addModes(scaleType, transposed, namer);
    }
  }

  private void addModes(ScaleType scaleType, Scale parent, ScaleUniverse.Namer namer) {
    Note oldRoot = parent.getRoot();
    
    Note majorRoot = scaleType.notationKey().apply(parent.getRoot());
    Accidental accidental = Accidental.fromMajorKey(majorRoot);
    KeySignature keySignature = KeySignature.fromScale(parent, majorRoot, accidental);
    int numberOfModes = this.includeModes ? parent.length() : 1;
    for (int i = 0; i < numberOfModes; i++) {
      Note newRoot = parent.getNote(i);
      Scale mode = parent.superimpose(newRoot);
      ScaleInfo info = ScaleInfo.builder()
          .keySignature(keySignature)
          .scale(mode)
          .parent(parent)
          .typeName(namer.typeName(i))
          .scaleName(namer.name(i, oldRoot, newRoot, keySignature.getAccidental()))
          .scaleType(scaleType)
          .build();
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

  // FIXME throw / return null instead? - NO, because need to find scales for unknown chords
  // FIXME too much guesswork here ...
  private ScaleInfo defaultInfo(Scale scale) {
    ScaleInfo info = ScaleInfo.builder().scale(scale).typeName(scale.asIntervals()).parent(scale).build();
    initializeDefaultInfoSuperAndSubScales(info);
    /*
     * TODO: should search for major scale with lowest index. 
     * E.g. FMaj7 belongs to F major scale, not to c major, although contained in both
     * If no major scale can be found, then meldodic / harmonic minor schould be searched. 
     */
    Optional<Scale> superScale = info.getSuperScales().stream().findFirst();
    KeySignature signature = superScale.isPresent() 
        ? info(superScale.get()).getKeySignature() 
        : KeySignature.fallback(scale, Accidental.fromMajorKey(scale.getRoot())); 
    info.setKeySignature(signature);
    info.setScaleName(scaleName(scale, signature));
    return info;
  }

  private String scaleName(Scale scale, KeySignature signature) {
    return isChord(scale) ? scale.asChord(signature.getAccidental()) : signature.toString(scale);
  }

  private void initializeDefaultInfoSuperAndSubScales(ScaleInfo info) {
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
