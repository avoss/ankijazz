package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TemplateTypeTest {

  @Test
  public void test() {
    String templateName = TemplateType.HTML.getTemplateName(TemplateTypeTest.class);
    assertEquals("de/jlab/scales/anki/TemplateTypeTest.html.mustache", templateName);
    Path outputPath = TemplateType.CSV.getOutputPath(Paths.get("build/anki"), TemplateTypeTest.class);
    assertEquals(Paths.get("build/anki/TemplateTypeTest.txt"), outputPath);
  }

}
