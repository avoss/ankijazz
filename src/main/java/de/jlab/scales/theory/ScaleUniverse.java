package de.jlab.scales.theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// TODO mode names not available in scales
public class ScaleUniverse implements Iterable<Scale> {
  private final List<Scale> allScales = new ArrayList<Scale>();
  private final Accidental accidental;

  public ScaleUniverse() {
    this(Accidental.FLAT);
  }

  public ScaleUniverse(Accidental accidentals) {
    this(accidentals, ScaleType.ALL);
  }

  public ScaleUniverse(Accidental accidentals, ScaleType ... includedScaleTypes) {
    this(accidentals, Arrays.asList(includedScaleTypes));
  }
  
  public ScaleUniverse(Accidental accidentals, Collection<? extends ScaleType> includedScaleTypes) {
    this.accidental = accidentals;
    for (ScaleType type : includedScaleTypes)
      createScales(type);
    initializeSubScales();
  }

  private void createScales(ScaleType type) {
    Scale scale = type.getScale();
    for (Note root : Note.values()) {
      Scale transposed = scale.transpose(root);
      // TODO this should be in Scale.transpose, not caller of transpose()
      transposed.setName(String.format("%s %s", root.getName(accidental), type.getName()));
      allScales.add(transposed);
    }
  }

  private void initializeSubScales() {
    for (Scale superScale : allScales) {
      Set<? extends Note> superSet = superScale.getNotes();
      for (Scale subScale : allScales) {
        Set<? extends Note> subSet = subScale.getNotes();
        if (superSet.equals(subSet))
          continue;
        if (superSet.containsAll(subSet)) {
          subScale.getSuperScales().add(superScale);
          superScale.getSubScales().add(subScale);
        }
      }
    }
  }

  @Override
  public Iterator<Scale> iterator() {
    return allScales.iterator();
  }

  public List<? extends Scale> getAllScales() {
    return allScales;
  }

}
