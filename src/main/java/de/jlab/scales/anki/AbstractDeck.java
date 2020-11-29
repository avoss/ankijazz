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
import java.util.concurrent.ThreadLocalRandom;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

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
    writeTo(dir, 0);
  }
  public void writeTo(Path dir, int shuffle) {
    try {
      Files.createDirectories(dir);
      writeHtml(dir);
      shuffle(shuffle);
      writeCsv(dir);
      cards.forEach(c -> c.writeAssets(dir));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void writeHtml(Path dir) throws IOException {
    Path path = dir.resolve(outputFileName.concat(".html"));
    Files.write(path, Collections.singleton(getHtml()));
  }

  private void writeCsv(Path dir) throws IOException {
    Path path = dir.resolve(outputFileName.concat(".txt"));
    Files.write(path, getCsv());
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
  
  private static class RandomDifficultyCard implements Comparable<RandomDifficultyCard> {
    private final Card card;
    private final int difficulty;
    RandomDifficultyCard(Card card, int randomness) {
      this.card = card;
      this.difficulty = card.getDifficulty() + ThreadLocalRandom.current().nextInt(randomness);
    }
    
    @Override
    public int compareTo(RandomDifficultyCard that) {
      return Integer.compare(this.difficulty, that.difficulty);
    }
  }
  
  @Override
  public void shuffle(int randomness) {
    if (randomness <= 0) {
      Collections.sort(cards);
      return;
    }
    List<RandomDifficultyCard> temp = new ArrayList<>();
    this.cards.stream().map(c -> new RandomDifficultyCard(c, randomness)).forEach (r -> temp.add(r));
    Collections.shuffle(temp);
    Collections.sort(temp);
    this.cards = temp.stream().map(r -> r.card).collect(toList()); 
  }

  @Override
  public List<Card> getCards() {
    return cards;
  }
}