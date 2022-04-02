package de.jlab.scales.sheet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ChordRenderer {

  interface Renderer {
    void renderSuper(String s);
    void renderNormal(String s);
    void renderSub(String s);
  }

  private final Renderer renderer;
  private final Pattern pattern;

  public ChordRenderer(Renderer renderer) {
    this.renderer = renderer;
    this.pattern = Pattern.compile("([A-G])([b#]?)(.*)");
  }

  public void parse(String string) {
    Matcher matcher = pattern.matcher(string);
    if (!matcher.find()) {
      throw new IllegalArgumentException("unable to parse " + string);
    }
    renderer.renderNormal(matcher.group(1));
    if (!matcher.group(2).isEmpty()) {
      renderer.renderSuper(matcher.group(2));
    } 
    if (!matcher.group(3).isEmpty()) {
      renderer.renderSub(matcher.group(3));
    } 
  }
}
