package com.ankijazz.midi.song;

import java.util.List;
import java.util.function.BiConsumer;

import com.ankijazz.midi.CompositePart;

public class BarProcessor implements BiConsumer<CompositePart, Bar> {
  private final List<BiConsumer<CompositePart, Bar>> consumers;

  public BarProcessor(List<BiConsumer<CompositePart, Bar>> consumers) {
    this.consumers = consumers;
  }

  @Override
  public void accept(CompositePart parts, Bar bar) {
    consumers.forEach(c -> c.accept(parts, bar));
  }

}