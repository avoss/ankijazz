package de.jlab.scales.anki;

import de.jlab.scales.Utils;

public class RhythmCard extends MustacheCardWithModel<RhythmModel> {

  public RhythmCard(RhythmModel model) {
    super(model);
  }
  
  public String getTags() {
    return Utils.tags();
  }
}
