package de.jlab.scales.anki;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.difficulty.DifficultyCollection;

public abstract class AbstractDeck implements Deck {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private final List<Card> cards = new ArrayList<>();
  private final String title;
  private final String outputFileName;
  private final String mustacheTemplate;
 
  protected AbstractDeck(String title) {
    this.title = title;
    this.outputFileName = getClass().getSimpleName();
    this.mustacheTemplate = String.format("%s.html.mustache", getClass().getName().replace('.', '/'));
  }

  protected AbstractDeck(String title, String outputFileName) {
    this.title = title;
    this.outputFileName = outputFileName;
    // FIXME duplicated code:
    this.mustacheTemplate = String.format("%s.html.mustache", getClass().getName().replace('.', '/'));
  }
  
  protected AbstractDeck(String title, String outputFileName, String mustacheTemplate, List<Card> cards) {
    this.title = title;
    this.outputFileName = outputFileName;
    this.mustacheTemplate = mustacheTemplate;
    this.cards.addAll(cards);
  }
  
  public String getTitle() {
    return title;
  }
  
  @Override
  public void add(Card card) {
    cards.add(card);
  }
  
  protected void addAll(List<Card> cards) {
    cards.addAll(cards);
  }
  

  @Override
  public void writeAssets(Path dir) {
    try {
      Files.createDirectories(dir);
      cards.forEach(c -> c.writeAssets(dir));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void writeAnki(Path dir) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve(outputFileName.concat(".txt"));
      Files.write(path, getCsv());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
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
  public void writeJson(Path dir) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve(outputFileName.concat(".json"));
      MAPPER.writeValue(path.toFile(), getCards().stream().map(Card::getJson).collect(Collectors.toList()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  @Override
  public String getHtml() {
    MustacheFactory factory = new DefaultMustacheFactory();
    Mustache mustache = factory.compile(mustacheTemplate);
    StringWriter writer = new StringWriter();
    mustache.execute(writer, this);
    return writer.toString();
  }
  
  
  @Override
  public List<String> getCsv() {
    return cards.stream().map(Card::getCsv).collect(toList());
  }
  
  @Override
  public List<String> getJson() {
    return cards.stream().map(Card::getJson).map(obj -> {
      try {
        return MAPPER.writeValueAsString(obj);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }).collect(toList());
  }
  
  @Override
  public void sort(double randomness) {
    DifficultyCollection.sort(cards, randomness);
  }

  @Override
  public List<Card> getCards() {
    return cards;
  }
  
  @Override
  public Deck subdeck(int numberOfCards) {
    Card[] cards = getCards().toArray(new Card[getCards().size()]);
    List<Card> sublist = new ArrayList<>();
    Interpolator interpolator = Utils.interpolator(0, numberOfCards, 0, cards.length);
    for (int i = 0; i < numberOfCards; i++) {
      sublist.add(cards[interpolator.apply(i)]);
    }
    return subdeck(title, outputFileName, mustacheTemplate, sublist);
  }

  protected Deck subdeck(String title, String outputFileName, String mustacheTemplate, List<Card> cards) {
    return new SimpleDeck(title, outputFileName, mustacheTemplate, cards);
  }
  
}