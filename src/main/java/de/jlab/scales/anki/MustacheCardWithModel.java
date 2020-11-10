package de.jlab.scales.anki;

import java.nio.file.Path;

public class MustacheCardWithModel<T extends WithDifficulty & WithAssets> extends MustacheCard {

  protected final T model;

  public MustacheCardWithModel(T model) {
    this.model = model;
  }

  public T getModel() {
    return model;
  }

  @Override
  public int getDifficulty() {
    return model.getDifficulty();
  }
  
  @Override
  public void writeAssets(Path directory) {
    model.writeAssets(directory);
  }
}
