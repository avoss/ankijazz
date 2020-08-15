package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltInScaleTypes.*;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyScale;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class ScaleCard implements Card {
  private static final ScaleUniverse universe = new ScaleUniverse(true, Major, MelodicMinor, HarmonicMinor);
  private final ScaleInfo modeInfo;
  private final ScaleInfo parentInfo;
  private final Accidental accidental;
  private final String uuid;

  public ScaleCard(Scale mode, Accidental accidental) {
    this.modeInfo = universe.info(mode);
    this.parentInfo = universe.info(modeInfo.getParent());
    this.accidental = accidental;
    this.uuid = Utils.uuid("AnkiJazz");
  }

  @Override
  public String[] getFields() {
    return new String[] { modeFullName(), modeTypeName(), modeRootName(), parentFullName(), parentTypeName(), parentRootName(), modePngName(), modeMp3Name() };
  }

  private String modeFullName() {
    return modeInfo.getName(accidental);
  }

  private String modeTypeName() {
    return modeInfo.getModeName();
  }

  private String modeRootName() {
    return modeInfo.getScale().getRoot().getName(accidental);
  }

  private String parentFullName() {
    return parentInfo.getName(accidental);
  }

  private String parentTypeName() {
    return parentInfo.getModeName();
  }

  private String parentRootName() {
    return parentInfo.getScale().getRoot().getName(accidental);
  }

  private String modePngName() {
    return uuid + ".png";
  }

  private String modeMp3Name() {
    return uuid + ".mp3";
  }

  public String lilyName() {
    return uuid + ".ly";
  }
  
  @Override
  public int getPriority() {
    return numberOfAccidentalsInParentScale();
  }

  private int numberOfAccidentalsInParentScale() {
    // we cannot check whether note is contained in CMajor because of Fb and B#
    // TODO: move to Scale?
    Scale parent = parentInfo.getScale();
    return (int) parent.stream().map(note -> parent.noteName(note, accidental)).filter(name -> name.contains("b") || name.contains("#")).count();
  }

  @Override
  public void writeAssets(Path dir) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve(lilyName());
      String lily = new LilyScale(modeInfo, accidental).toLily();
      Files.write(path, asList(lily));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
