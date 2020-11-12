package de.jlab.scales.anki;

import de.jlab.scales.Utils;

public class ModesPracticeGuitarCard extends MustacheCardWithModel<ScaleModel> {

  private FretboardPosition position;

  public ModesPracticeGuitarCard(ScaleModel model, FretboardPosition position) {
    super(model);
    this.position = position;
  }
  
  public FretboardPosition getPosition() {
    return position;
  }

  public String getTags() {
    return Utils.tags(model.getModeRootName(), model.getModeTypeName(), model.getDirection(), position.getLabel() + " Position");
  }
  
}
