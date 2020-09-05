package de.jlab.scales.anki;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SimpleDeck implements Deck {
  private List<Card> cards = new ArrayList<>();
  private String id;
  private int counter;
  
  public SimpleDeck(String id) {
    this.id = "AnkiJazz-" + id;
  }
 
  @Override
  public void add(Card card) {
    card.setId(String.format("%s-%04d", id, counter++));
    cards.add(card);
  }

  @Override
  public void writeTo(Path dir) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve("anki.txt");
      List<String> rows = getCsv();
      Files.write(path, rows);
      cards.forEach(c -> c.writeAssets(dir));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
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
  public List<String> getCsv() {
    return cards.stream().map(Card::getFields).map(this::toCsv).collect(toList());
  }

  private String toCsv(String... fields) {
    return Stream.of(fields).collect(joining(";"));
  }

  @Override
  public List<Card> getCards() {
    return cards;
  }
}