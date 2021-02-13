package de.jlab.scales.anki;

import static de.jlab.scales.anki.AnkiUtils.ankiMp3;
import static de.jlab.scales.anki.AnkiUtils.ankiPng;
import static java.util.stream.Collectors.joining;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.jlab.scales.Utils;
import de.jlab.scales.difficulty.Difficulties;
import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.ScaleInfo;

public class FretboardDiagramCard implements Card {

  private String assetId;
  private Supplier<BufferedImage> frontImage;
  private Supplier<BufferedImage> backImage;
  private Supplier<Part> backMidi;
  private ScaleInfo chordInfo;
  private ScaleInfo scaleInfo;
  private double difficulty;
  private String title;
  private int fretNumber;
  private int stringNumber;
  private String comment;

  @lombok.Builder
  private FretboardDiagramCard(String title, String comment, int fretNumber, int stringNumber, ScaleInfo chordInfo, ScaleInfo scaleInfo, Supplier<BufferedImage> frontImage, Supplier<BufferedImage> backImage, Supplier<Part> backMidi) {
    this.title = title;
    this.comment = comment;
    this.fretNumber = fretNumber;
    this.stringNumber = stringNumber;
    this.chordInfo = chordInfo;
    this.scaleInfo = scaleInfo;
    this.frontImage = frontImage;
    this.backImage = backImage;
    this.backMidi = backMidi;
    this.assetId = Utils.uuid(); //computeAssetId();
    this.difficulty = computeDifficulty();
  }

  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(40).update(Difficulties.getChordInfoDifficulty(chordInfo));
    model.doubleTerm(60).update(Difficulties.getScaleInfoDifficulty(scaleInfo));
    return model.getDifficulty();
  }

  @Override
  public double getDifficulty() {
    return difficulty;
  }

  @Override
  public String getAssetId() {
    return assetId;
  }

  @Override
  public void writeAssets(Path directory) {
    Utils.writeImage(directory.resolve(getFrontPngName()), frontImage.get());
    Utils.writeImage(directory.resolve(getBackPngName()), backImage.get());
    Utils.writeMidi(directory.resolve(backMidiName()), backMidi.get());
  }


  @Override
  public String getCsv() {
    return Stream.of(
        getTitle(),
        getComment(),
        getFretNumber(),
        getStringNumber(),
        getScaleName(),
        getScaleTypeName(),
        getScaleRootName(),
        getChordName(),
        getChordTypeName(),
        getChordRootName(),
        ankiPng(getFrontPngName()),
        ankiPng(getBackPngName()),
        ankiMp3(getBackMp3Name())
        ).collect(joining(CSV_DELIMITER));
  }

  @Override
  public Map<String, Object> getJson() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("title", getTitle());
    map.put("comment", getComment());
    map.put("fretNumber", getFretNumber());
    map.put("stringNumber", getStringNumber());
    map.put("scaleName", getScaleName());
    map.put("scaleTypeName", getScaleTypeName());
    map.put("scaleRootName", getScaleRootName());
    map.put("chordName", getChordName());
    map.put("chordTypeName", getChordTypeName());
    map.put("chordRootName", getChordRootName());
    map.put("frontPngName", getFrontPngName());
    map.put("backPngName", getBackPngName());
    map.put("backMp3Name", getBackMp3Name());
    
    return map;
  }
  
  public String getChordRootName() {
    return getRoot(chordInfo);
  }

  public String getChordTypeName() {
    return chordInfo.getScaleType().getTypeName();
  }

  public String getChordName() {
    return chordInfo.getScaleName();
  }

  public String getScaleRootName() {
    return getRoot(scaleInfo);
  }

  private String getRoot(ScaleInfo info) {
    return info.getKeySignature().notate(info.getScale().getRoot());
  }

  public String getScaleTypeName() {
    return scaleInfo.getScaleType().getTypeName();
  }

  public String getScaleName() {
    return scaleInfo.getScaleName();
  }

  public String getTitle() {
    return title;
  }
  
  public String getFrontPngName() {
    return assetId.concat("f.png");
  }
  public String getBackPngName() {
    return assetId.concat(".png");
  }
  public String backMidiName() {
    return assetId.concat(".midi");
  }
  public String getBackMp3Name() {
    return assetId.concat(".mp3");
  }
  public ScaleInfo getChordInfo() {
    return chordInfo;
  }
  public ScaleInfo getScaleInfo() {
    return scaleInfo;
  }
  public String getFretNumber() {
    return Integer.toString(fretNumber);
  }
  public String getStringNumber() {
    return Integer.toString(stringNumber + 1);
  }
  public String getComment() {
    return comment;
  }
}
