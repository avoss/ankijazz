package de.jlab.scales.anki;

import static java.lang.String.format;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Deck {
  List<String> cards = new ArrayList<>();

  void add(String question, String answer) {
    cards.add(format("%s;%s", question, answer));
  }

  public void writeTo(Path path) {
    try {
      //Collections.shuffle(cards);
      Files.write(path, cards);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public List<String> getCards() {
    return cards;
  }
}