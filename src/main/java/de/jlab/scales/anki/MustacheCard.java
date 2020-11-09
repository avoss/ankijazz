package de.jlab.scales.anki;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MustacheCard<T extends HasDifficulty> implements Card {
  
  private final T model;
  private final String templateBaseName;
  
  private enum TemplateType {
    CSV(".csv.mustache"),
    HTML(".html.mustache");

    private String extension;

    TemplateType(String extension) {
      this.extension = extension;
    }
    public String getTemplateName(String templateBaseName) {
      return templateBaseName + extension;
    }
  }
  
  public MustacheCard(T model, String templateBaseName) {
    this.model = model;
    this.templateBaseName = templateBaseName;
  }
  
  public T getModel() {
    return model;
  }

  @Override
  public int getDifficulty() {
    return model.getDifficulty();
  }


  @Override
  public String toCsv() {
    String result = transform(TemplateType.CSV);
    result = result.replaceAll("\\s+", " ");
    result = result.replaceAll("\\s*;\\s*", ";");
    return result.trim();
  }

  private String transform(TemplateType type) {
    try {
      MustacheFactory factory = new DefaultMustacheFactory();
      Mustache mustache = factory.compile(type.getTemplateName(templateBaseName));
      StringWriter writer = new StringWriter();
      mustache.execute(writer, this);
      writer.close();
      return writer.toString();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
}
