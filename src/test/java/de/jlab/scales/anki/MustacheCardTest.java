package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

public class MustacheCardTest {

  static class TestModel implements HasDifficulty {

    private final String front = "A < B?";
    private final String back = "Yes";
    private final String tags = "A B";

    @Override
    public int getDifficulty() {
      return 0;
    }

    public String getFront() {
      return front;
    }

    public String getBack() {
      return back;
    }

    public String getTags() {
      return tags;
    }
  }

  private MustacheCard<TestModel> card = new MustacheCard<>(new TestModel(), "MustacheCard");
  
  @Test
  public void testToCsv() {
    assertEquals("<div> Question: <b>A &lt; B?</b> </div>;<div> Answer: <b>Yes</b> </div>;A B", card.toCsv());
  }

  @Test
  public void testToHtml() {
    assertEquals("<h1>Heading</h1> <div> Question: <b>A &lt; B?</b> </div> <div> Answer: <b>Yes</b> </div> <div> Tags: A B </div>", card.toHtml());
  }
}
