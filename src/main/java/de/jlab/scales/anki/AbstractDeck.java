package de.jlab.scales.anki;

import static de.jlab.scales.anki.TemplateType.HTML;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import de.jlab.scales.difficulty.DifficultyCollection;

public abstract class AbstractDeck implements Deck {
  
  private List<Card> cards = new ArrayList<>();
  private final String title;
  private String outputFileName;
 
  protected AbstractDeck(String title) {
    this.outputFileName = getClass().getSimpleName();
    this.title = title;
  }
  
  protected AbstractDeck(String outputFileName, String title) {
    this.outputFileName = outputFileName;
    this.title = title;
  }
  
  public String getTitle() {
    return title;
  }
  
  @Override
  public void add(Card card) {
    cards.add(card);
  }

  @Override
  public void writeTo(Path dir) {
    try {
      Files.createDirectories(dir);
      writeCsv(dir);
      cards.forEach(c -> c.writeAssets(dir));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void writeCsv(Path dir) throws IOException {
    Path path = dir.resolve(outputFileName.concat(".txt"));
    Files.write(path, getCsv());
  }

  @Override
  public void writeHtml(Path dir) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve(outputFileName.concat(".html"));
      Files.write(path, Collections.singleton(getHtml()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  @Override
  public String getHtml() {
    MustacheFactory factory = new DefaultMustacheFactory();
    Mustache mustache = factory.compile(HTML.getTemplateName(getClass()));
    StringWriter writer = new StringWriter();
    mustache.execute(writer, this);
    return writer.toString();
  }
  
  
  @Override
  public List<String> getCsv() {
    return cards.stream().map(Card::getCsv).collect(toList());
  }
  
  @Override
  public void sort(double randomness) {
    DifficultyCollection.sort(cards, randomness);
  }

  @Override
  public List<Card> getCards() {
    return cards;
  }
}