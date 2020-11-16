package de.jlab.scales.anki;

import static java.lang.String.format;

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
    return Utils.tags(
        format("KeyOf %s", model.getModeRootName()), 
        format("Mode %s", model.getModeTypeName()), 
        format("Direction %s", model.getDirection()),
        format("Clef %s", model.getClef()),
        format("Instrument %s", model.getInstrument()),
        format("Position %s", position.getLabel()));
  }
  
}
