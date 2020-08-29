package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyScale;
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
    this.priority = keySignature.getNumberOfAccidentals() + ThreadLocalRandom.current().nextInt(SHUFFLE);
  }

  @Override
  public String[] getFields() {
    return new String[] { modeName(), noteNames(), modeTypeName(), modeRootName(), parentName(), parentTypeName(), parentRootName(), modePngName(), modeMp3Name() };
  }

  private String noteNames() {
    return keySignature.toString(modeInfo.getScale());
  }

  private String modeName() {
    return modeInfo.getScaleName();
  }

  private String modeTypeName() {
    return modeInfo.getTypeName();
  }

  private String modeRootName() {
    return keySignature.notate(modeInfo.getScale().getRoot());
  }

  private String parentName() {
    return parentInfo.getScaleName();
  }

  private String parentTypeName() {
    return parentInfo.getTypeName();
  }

  private String parentRootName() {
    return keySignature.notate(parentInfo.getScale().getRoot());
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
