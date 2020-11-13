package de.jlab.scales.anki;

import de.jlab.scales.Utils;

public class RhythmCard extends MustacheCardWithModel<ScaleModel> {

  public RhythmCard(ScaleModel model) {
    super(model);
  }
  
  public String getTags() {
    return Utils.tags(model.getModeRootName(), model.getModeTypeName(), model.getDirection());
  }
}
