package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

public interface Deck {
  default void add(String... fields) {
    add(0, fields);
  }
  
  default void add(int priority, String... fields) {
    add(new BasicCard(priority, fields));
  }

  void add(Card card);

  void writeTo(Path dir);

  void shuffle(int randomness);

  List<String> getCsv();

  List<Card> getCards();

}