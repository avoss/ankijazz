package de.jlab.scales.anki;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import de.jlab.scales.Utils;

public class RhythmCard extends MustacheCardWithModel<RhythmModel> {

  public RhythmCard(RhythmModel model) {
    super(model);
  }
  
  public String getTags() {
    List<String> tags = new ArrayList<>();
    if (model.hasTies()) {
      tags.add("Rhythm with ties");
    }
    tags.add(format("Rhythm %d", model.getNumberOfUniqueSequences()));
    tags.add(format("Rhythm %s", model.getTypeName()));    
    return Utils.tags(tags);
  }
}
