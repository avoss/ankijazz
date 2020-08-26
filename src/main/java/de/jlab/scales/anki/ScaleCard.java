package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyScale;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class ScaleCard implements Card {
  private static final ScaleUniverse universe = new ScaleUniverse(true, Major, MelodicMinor, HarmonicMinor);
  private final ScaleInfo modeInfo;
  private final ScaleInfo parentInfo;
  private final String uuid;
  private KeySignature keySignature;
  private int priority;
  static int SHUFFLE = 3;

  public ScaleCard(Scale mode, KeySignature keySignature) {
    this.keySignature = keySignature;
    this.modeInfo = universe.info(mode);
    this.parentInfo = universe.info(modeInfo.getParent());
    this.uuid = Utils.uuid("AnkiJazz");
    this.priority = numberOfAccidentalsInParentScale() + ThreadLocalRandom.current().nextInt(SHUFFLE);
  }

  @Override
  public String[] getFields() {
    return new String[] { modeFullName(), noteNames(), modeTypeName(), modeRootName(), parentFullName(), parentTypeName(), parentRootName(), modePngName(), modeMp3Name() };
  }

  private String noteNames() {
    return modeInfo.getScale().asScale(keySignature.getAccidental());
  }

  private String modeFullName() {
    return modeInfo.getName(keySignature.getAccidental());
  }

  private String modeTypeName() {
    return modeInfo.getModeName();
  }

  private String modeRootName() {
    return modeInfo.getScale().getRoot().getName(keySignature.getAccidental());
  }

  private String parentFullName() {
    return parentInfo.getName(keySignature.getAccidental());
  }

  private String parentTypeName() {
    return parentInfo.getModeName();
  }

  private String parentRootName() {
    return parentInfo.getScale().getRoot().getName(keySignature.getAccidental());
  }

  private String modePngName() {
    return format("<img src=\"%s.png\">", uuid);
  }

  private String modeMp3Name() {
    return format("[sound:%s.mp3]",  uuid);
  }

  public String lilyName() {
    return uuid + ".ly";
  }
  
  @Override
  public int getPriority() {
    return priority;
  }

  private int numberOfAccidentalsInParentScale() {
    // we cannot check whether note is contained in CMajor because of Fb,Cb and B#,E#
    // TODO: move to Scale?
    Scale parent = parentInfo.getScale();
    return (int) parent.stream().map(note -> parent.noteName(note, keySignature.getAccidental())).filter(name -> name.contains("b") || name.contains("#")).count();
  }

  @Override
  public void writeAssets(Path dir) {
    try {
      Path path = dir.resolve(lilyName());
      String lily = new LilyScale(modeInfo, keySignature).toLily();
      Files.write(path, asList(lily));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
