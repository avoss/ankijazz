package com.ankijazz.anki;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

/**
 * In iRealPro export setlist (e.g. Jazz 1400) as Setlist and as PDF, this will create 'Jazz 1400.txt' and 'Jazz 1400.pdf'. 
 * Run
 *  
 *      pdftoppm -png 'Jazz 1400.pdf' 'Jazz1400' 
 *      
 * this will generate files Jazz1400-0001.png up to Jazz1400-1400.png. Note that Anki does not support spaces in image file names.
 * 
 */
public class IRealDeckProd {
  
  final Path workdir = Path.of("build", "tmp");
  final String setlist = "Jazz 1400";
//  final String setlist = "Brazilian 150";
  final String imagePrefix = setlist.replaceAll("\\s+", "");
  
  @Test
  @Ignore
  public void generateIRealPro() throws IOException {
    SimpleDeck<SimpleCard> deck = new SimpleDeck<SimpleCard>(setlist, setlist.concat(" Deck"));
    
    Pattern pattern = Pattern.compile("(\\d+) - (.*) \\((.*)\\)\\n");
    String string = Files.readString(workdir.resolve(setlist.concat(".txt")));
    Matcher matcher = pattern.matcher(string);
    while (matcher.find()) {
      int page = Integer.parseInt(matcher.group(1));
      String title = matcher.group(2);
      deck.add(simpleCard(title, page));
    }
    deck.writeAnki(workdir);
  }

  private SimpleCard simpleCard(String title, int page) {
    SimpleCard card = new SimpleCard(1.0, "FRONT", "BACK");
    card.put("FRONT", String.format("<h1>%s</h1>", title));
    // TODO: number of digits depends on deck size!!
    card.put("BACK", String.format("<img src=\"%s-%04d.png\">", imagePrefix, page));
    return card;
  }
}
