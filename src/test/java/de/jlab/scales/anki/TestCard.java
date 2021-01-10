package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.Map;

public class TestCard extends MustacheCard {

  public static class TestModel {

    private final String front = "A < B?";
    private final String back = "Yes";
    private final String tags = "A B";

    public String getFront() {
      return front;
    }

    public String getBack() {
      return back;
    }

    public String getTags() {
      return tags;
    }

  }

  private TestModel model = new TestModel();

  @Override
  public double getDifficulty() {
    return 0;
  }

  @Override
  public void writeAssets(Path directory) {
  }
  
  @Override
  public String getAssetId() {
    return TestCard.class.getName();
  }

  public TestModel getModel() {
    return model;
  }

  @Override
  public Map<String, Object> getJson() {
    throw new UnsupportedOperationException();
  }

}
