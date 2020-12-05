package de.jlab.scales.theory;

import static de.jlab.scales.Utils.isSymmetricalDuplicate;
import static de.jlab.scales.theory.BuiltinScaleType.*;
import static java.util.stream.Collectors.toList;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

public class ScaleUniverse implements Iterable<Scale> {

  public static List<ScaleType> SCALE_TYPES = List.of(BuiltinScaleType.values());
  public static List<ScaleType> CHORD_TYPES = List.of(BuiltinChordType.values());
  public static List<ScaleType> ALL_TYPES = Stream.of(BuiltinScaleType.values(), BuiltinChordType.values()).flatMap(Arrays::stream).collect(toList());

  public static final ScaleUniverse MODES = new ScaleUniverse(true, SCALE_TYPES);
  public static final ScaleUniverse CHORDS = new ScaleUniverse(false, CHORD_TYPES);
  
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

    public String name(int index, Note oldRoot, Note newRoot, KeySignature keySignature) {

      String oldRootName = keySignature.notate(oldRoot);
      String newRootName = keySignature.notate(newRoot);
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
  private Set<Scale> scales = new LinkedHashSet<>();
  private boolean includeModes;

  public ScaleUniverse(boolean includeModes, Collection<? extends ScaleType> scaleTypes) {
    this.includeModes = includeModes;
    scaleTypes.stream().forEachOrdered(this::add);
    initializeSubScales();
  }

  private void add(ScaleType scaleType) {
    if (scaleType.isChord()) {
      chord(scaleType);
    } else {
      scale(scaleType);
    }
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
    Scale prototype = scaleType.getPrototype();
    for (Scale parent : Scales.allKeys(prototype)) {
      Note notationKey = scaleType.notationKey().apply(parent.getRoot());
      Accidental accidental = Accidental.preferredAccidentalForMajorKey(notationKey);
      KeySignature keySignature = KeySignature.fromScale(parent, notationKey, accidental);
      addModes(scaleType, parent, namer, keySignature);
      if (notationKey == Note.Gb) {
        addModes(scaleType, parent, namer, keySignature.inverse());
      }
    }
  }

  private void addModes(ScaleType scaleType, Scale parent, Namer namer, KeySignature keySignature) {
    Note oldRoot = parent.getRoot();
    
    int numberOfModes = this.includeModes ? parent.getNumberOfNotes() : 1;
    ScaleInfo parentInfo = null;
    for (int i = 0; i < numberOfModes; i++) {
      Note newRoot = parent.getNote(i);
      Scale mode = parent.superimpose(newRoot);
      if (isSymmetricalDuplicate(parent, mode)) {
        break;
      }
      ScaleInfo info = ScaleInfo.builder()
          .keySignature(keySignature)
          .scale(mode)
          .parentInfo(parentInfo)
          .typeName(namer.typeName(i))
          .scaleName(namer.name(i, oldRoot, newRoot, keySignature))
          .scaleType(scaleType)
          .build();
      if (parentInfo == null) {
        parentInfo = info;
        info.setParentInfo(parentInfo);
      }
      infos.put(mode, info);
      scales.add(mode);
    }
  }

  private final Comparator<ScaleInfo> difficulty = new Comparator<ScaleInfo>() {
    private final List<ScaleType> types = List.of(Major, MelodicMinor, HarmonicMinor, WholeTone, DiminishedHalfWhole, HarmonicMajor);

    @Override
    public int compare(ScaleInfo a, ScaleInfo b) {
      if (a.isInversion() != b.isInversion()) {
        return a.isInversion() ? 1 : -1;
      }
      if (a.getScaleType() != b.getScaleType()) {
        return Integer.compare(indexOf(a), indexOf(b));
      }
      return Integer.compare(a.getKeySignature().getNumberOfAccidentals(), b.getKeySignature().getNumberOfAccidentals()); 
    }

    private int indexOf(ScaleInfo info) {
      int index = types.indexOf(info);
      return index >= 0 ? index : 1000;
    }
  };

  public List<ScaleInfo> infos(Scale scale) {
    return infos.get(scale).stream().sorted(difficulty).collect(toList());
  }

  // TODO rename to findFirstOrElseDefault
  public ScaleInfo info(Scale scale) {
    return infos(scale).stream().findFirst().orElseGet(() -> defaultInfo(scale));
//    return infos(scale).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Scale not found: " + scale));
  }

  public List<ScaleInfo> findScalesContaining(Set<Note> notes) {
    return scales.stream()
       .filter(scale -> scale.asSet().containsAll(notes))
       .flatMap(scale -> infos(scale).stream())
       .filter(info -> !info.isInversion())
       .sorted(difficulty)
       .collect(Collectors.toList());
    
  }
  
  // FIXME too much guesswork here ...
  private ScaleInfo defaultInfo(Scale scale) {
    ScaleInfo info = ScaleInfo.builder().scale(scale).typeName(scale.asIntervals()).build();
    info.setParentInfo(info);
    initializeDefaultInfoSuperAndSubScales(info);
    /*
     * TODO: should search for major scale with lowest index. 
     * E.g. FMaj7 belongs to F major scale, not to c major, although contained in both
     * If no major scale can be found, then meldodic / harmonic minor schould be searched. 
     */
    Optional<Scale> superScale = info.getSuperScales().stream().findFirst();
    KeySignature signature = superScale.isPresent() 
        ? info(superScale.get()).getKeySignature() 
        : KeySignature.fromScale(scale); 
    info.setKeySignature(signature);
    info.setScaleName(scaleName(scale, signature));
    return info;
  }

  private String scaleName(Scale scale, KeySignature signature) {
    return isChord(scale) ? scale.asChord(signature.getAccidental()) : signature.toString(scale);
  }
  
  private boolean isChord(Scale scale) {
    return scale.getNumberOfNotes() < 5;
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
