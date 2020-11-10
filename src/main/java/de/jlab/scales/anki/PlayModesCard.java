package de.jlab.scales.anki;

public class PlayModesCard extends MustacheCardWithModel<ScaleModel> {

  public PlayModesCard(ScaleModel model) {
    super(model);
  }
  
  public String getTags() {
    return tags(model.getModeRootName(), model.getModeTypeName(), model.getDirection());
  }
}
