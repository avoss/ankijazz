package de.jlab.scales.anki;

import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class ScaleCard implements Card {
  private final ScaleInfo scaleInfo;
  private final Accidental accidental;

  public ScaleCard(ScaleInfo scaleInfo, Accidental accidental) {
    this.scaleInfo = scaleInfo;
    this.accidental = accidental;
  }
  
  @Override
  public String[] getFields() {
    // png name + mp3 name: use hash on scale? on scaleInfo w/ accidental?
    return new String[] {scaleName(), parentName(), modeName(), keyName(), pngSnippet(), mp3Snippet()};
  }

  @Override
  public double getPriority() {
    // TODO Auto-generated method stub
    return 0;
  }
}
