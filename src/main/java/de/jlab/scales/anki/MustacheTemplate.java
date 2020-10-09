package de.jlab.scales.anki;

public class MustacheTemplate {

  private final String name;

  public MustacheTemplate(String name) {
    this.name = name;
  }
  
  public String getTemplateName() {
    return name + ".mustache";
  }

  public String getHtmlName() {
    return name + ".html";
  }

}
