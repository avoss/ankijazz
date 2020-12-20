package de.jlab.scales.anki;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public abstract class MustacheCard implements Card {

  private final String mustacheTemplate;

  protected MustacheCard(String mustacheTemplate) {
    this.mustacheTemplate = mustacheTemplate;
  }
  
  protected MustacheCard() {
    this.mustacheTemplate = getClass().getName().replace('.', '/');
  }
  
  @Override
  public String getCsv() {
    return transform(".csv.mustache").replace(CSV_DELIMITER, " ").replaceAll("\\s*-----[-]+\\s*", CSV_DELIMITER);
  }

  public String getHtml() {
    return transform(".html.mustache");
  }

  private String transform(String extension) {
    try {
      MustacheFactory factory = new DefaultMustacheFactory();
      Mustache mustache = factory.compile(mustacheTemplate.concat(extension));
      StringWriter writer = new StringWriter();
      mustache.execute(writer, this);
      writer.close();
      return writer.toString().replaceAll("\\s+", " ").trim();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
}
