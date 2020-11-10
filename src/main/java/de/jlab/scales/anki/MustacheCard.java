package de.jlab.scales.anki;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MustacheCard<T extends WithDifficulty> implements Card {

  private final T model;

  public MustacheCard(T model) {
    this.model = model;
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
    return transform(TemplateType.CSV).replaceAll("\\s*-----[-]+\\s*", ";");
  }

  @Override
  public String toHtml() {
    return transform(TemplateType.HTML);
  }

  private String transform(TemplateType type) {
    try {
      MustacheFactory factory = new DefaultMustacheFactory();
      Mustache mustache = factory.compile(type.getTemplateName(getClass()));
      StringWriter writer = new StringWriter();
      mustache.execute(writer, this);
      writer.close();
      return writer.toString().replaceAll("\\s+", " ").trim();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
