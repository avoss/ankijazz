package de.jlab.scales.anki;

public class PlayModesGuitarCard extends MustacheCardWithModel<ScaleModel> {

  private FretboardPosition position;

  public PlayModesGuitarCard(ScaleModel model, FretboardPosition position) {
    super(model);
    this.position = position;
  }
  
  public FretboardPosition getPosition() {
    return position;
  }

}
