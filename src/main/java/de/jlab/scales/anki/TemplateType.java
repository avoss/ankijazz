package de.jlab.scales.anki;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum TemplateType {
  CSV("csv.mustache", "txt"), HTML("html.mustache", "html");

  private String templateExtension;
  private String outputExtension;

  TemplateType(String templateExtension, String outputExtension) {
    this.templateExtension = templateExtension;
    this.outputExtension = outputExtension;
  }

  /**
   * to be used with ClassLoader.getResource
   */
  public String getTemplateName(Class<?> clazz) {
    return String.format("%s.%s", clazz.getName().replace('.', '/'), templateExtension);
  }
  
  public Path getOutputPath(Path dir, Class<?> clazz) {
    return dir.resolve(clazz.getSimpleName().concat(".").concat(outputExtension));
  }
}