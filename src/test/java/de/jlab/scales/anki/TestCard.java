package de.jlab.scales.anki;

import java.nio.file.Path;

public class TestCard extends MustacheCard {

  private TestModel model = new TestModel();

  @Override
  public double getDifficulty() {
    return 0;
  }

  @Override
  public void writeAssets(Path directory) {
  }

  public TestModel getModel() {
    return model;
  }

}
