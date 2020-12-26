package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MustacheCardTest {

  private TestCard card = new TestCard();

  @Test
  public void testToCsv() {
    assertEquals("<div> Question: <b>A &lt; B?</b> </div>\t<div> Answer: <b>Yes</b> </div>\tA B", card.getCsv());
  }

  @Test
  public void testToHtml() {
    assertEquals("<h1>Heading</h1> <div> Question: <b>A &lt; B?</b> </div> <div> Answer: <b>Yes</b> </div> <div> Tags: A B </div>", card.getHtml());
  }
}