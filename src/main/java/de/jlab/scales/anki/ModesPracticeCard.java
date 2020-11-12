package de.jlab.scales.anki;

import de.jlab.scales.Utils;

public class ModesPracticeCard extends MustacheCardWithModel<ScaleModel> {

  public ModesPracticeCard(ScaleModel model) {
    super(model);
  }
  
  public String getTags() {
    return Utils.tags(model.getModeRootName(), model.getModeTypeName(), model.getDirection());
  }
}
