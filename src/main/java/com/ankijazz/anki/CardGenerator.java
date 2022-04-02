package de.jlab.scales.anki;

import java.util.Collection;

public interface CardGenerator<T extends Card> {
  String getTitle();
  String getFileName();
  Collection<? extends T> generate();
}
