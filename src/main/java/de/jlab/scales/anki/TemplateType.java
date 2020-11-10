package de.jlab.scales.anki;

public enum TemplateType {
  CSV("csv.mustache"), HTML("html.mustache");

  private String extension;

  TemplateType(String extension) {
    this.extension = extension;
  }

  public String getTemplateName(Class<?> clazz) {
    return String.format("%s.%s", clazz.getName().replace('.', '/'), extension);
  }
}