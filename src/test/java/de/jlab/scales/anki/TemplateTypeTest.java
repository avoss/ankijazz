package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemplateTypeTest {

  @Test
  public void test() {
    String templateName = TemplateType.HTML.getTemplateName(TemplateTypeTest.class);
    assertEquals("de/jlab/scales/anki/TemplateTypeTest.html.mustache", templateName);
  }

}
