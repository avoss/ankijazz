package de.jlab.scales.anki;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import de.jlab.scales.Utils;
import de.jlab.scales.midi.Part;

public class FretboardDiagramCard implements Card {

  private String assetId;
  private String chordName;
  private Supplier<BufferedImage> frontImage;
  private Supplier<BufferedImage> backImage;
  private Supplier<Part> backMidi;

  public FretboardDiagramCard(String chordName, Supplier<BufferedImage> frontImage, Supplier<BufferedImage> backImage, Supplier<Part> backMidi) {
    this.chordName = chordName;
    this.frontImage = frontImage;
    this.backImage = backImage;
    this.backMidi = backMidi;
    assetId = Utils.uuid();
  }

  @Override
  public double getDifficulty() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getAssetId() {
    return assetId;
  }

  public String getChordName() {
    return chordName;
  }
  public String frontPngName() {
    return assetId.concat("-Front.png");
  }
  public String backPngName() {
    return assetId.concat("-Back.png");
  }
  public String backMidiName() {
    return assetId.concat("-Back.midi");
  }
  public String backMp3Name() {
    return assetId.concat("-Back.mp3");
  }
  
  @Override
  public void writeAssets(Path directory) {
    Utils.writeImage(directory.resolve(frontPngName()), frontImage.get());
    Utils.writeImage(directory.resolve(backPngName()), backImage.get());
    Utils.writeMidi(directory.resolve(backMidiName()), backMidi.get());
  }


  @Override
  public String getCsv() {
    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public Map<String, Object> getJson() {
    // TODO Auto-generated method stub
    return Collections.emptyMap();
  }

}
