package de.jlab.scales.anki;

public enum TemplateType {
  CSV("csv.mustache"), HTML("html.mustache");

  private String templateExtension;

  TemplateType(String templateExtension) {
    this.templateExtension = templateExtension;
  }

  /**
   * to be used with ClassLoader.getResource
   */
  public String getTemplateName(Class<?> clazz) {
    return String.format("%s.%s", clazz.getName().replace('.', '/'), templateExtension);
  }
  
}