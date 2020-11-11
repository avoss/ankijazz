package de.jlab.scales.anki;

import de.jlab.scales.Utils;

public class PlayModesCard extends MustacheCardWithModel<ScaleModel> {

  public PlayModesCard(ScaleModel model) {
    super(model);
  }
  
  public String getTags() {
    return Utils.tags(model.getModeRootName(), model.getModeTypeName(), model.getDirection());
  }
}
