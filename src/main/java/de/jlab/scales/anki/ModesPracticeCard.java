package de.jlab.scales.anki;

import static java.lang.String.format;

import de.jlab.scales.Utils;

public class ModesPracticeCard extends MustacheCardWithModel<ScaleModel> {

  public ModesPracticeCard(ScaleModel model) {
    super(model);
  }
  
  public String getTags() {
    return Utils.tags(
        format("KeyOf %s", model.getModeRootName()), 
        format("Mode %s", model.getModeTypeName()), 
        format("Direction %s", model.getDirection()),
        format("Clef %s", model.getClef()),
        format("Instrument %s", model.getInstrument())
     );
  }
}
