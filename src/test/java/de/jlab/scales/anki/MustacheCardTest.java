package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

public class MustacheCardTest {
  
  static class TestModel implements HasDifficulty {
    
    private final String front = "Question";
    private final String back = "Answer";
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

  @Test
  public void testToCsv() {
    MustacheCard<TestModel> card = new MustacheCard<>(new TestModel(), "MustacheCard");
    assertEquals("<div> This is the <b>Question</b> </div>;<div> Cool, an <b>Answer</b> </div>;A B", card.toCsv());
  }

}
