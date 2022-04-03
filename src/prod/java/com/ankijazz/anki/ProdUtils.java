package com.ankijazz.anki;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProdUtils {
  public static <T extends Card> Deck<T> writeTo(Deck<T> deck, double randomness) {
    deck.sort(randomness);
    Path ankiDir = Paths.get("build/anki");
    deck.writeAnki(ankiDir); 
    deck.writeHtml(ankiDir);
    deck.writeJson(ankiDir);
    deck.writeAssets(ankiDir);
    
    return writePreviewOnly(deck, randomness);
  }

  public static <T extends Card> Deck<T> writePreviewOnly(Deck<T> deck, double randomness) {
    Deck<T> subdeck = deck.subdeck(25);
    subdeck.sort(randomness);
    Path previewDir = Paths.get("build/preview");
    subdeck.writeAnki(previewDir); 
    subdeck.writeHtml(previewDir);
    subdeck.writeJson(previewDir);
    subdeck.writeAssets(previewDir);
    return subdeck;
  }

}
