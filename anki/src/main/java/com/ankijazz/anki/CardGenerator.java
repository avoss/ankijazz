package com.ankijazz.anki;

import java.util.Collection;

public interface CardGenerator<T extends Card> {
  String getTitle();
  String getFileName();
  Collection<? extends T> generate();
}
