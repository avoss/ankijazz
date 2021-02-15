package de.jlab.scales.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.jlab.scales.TestUtils;

public class PreviewJsonCombinerTest {

  private static final String COMBINED_JSON = "combined.json";
  private static final Map<String, Map<String, String>> B1 = Map.of("u", Map.of("a", "b", "c", "d"), "v", Map.of("e", "f", "g", "h"));
  private static final Map<String, Integer> A2 = Map.of("c", 3, "d", 4);
  private static final Map<String, Integer> A1 = Map.of("a", 1, "b", 2);
  private static final String B_JSON = "b.json";
  private static final String A_JSON = "a.json";
  private final Path dir = TestUtils.outputDir(PreviewJsonCombinerTest.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();
  
  
  @Test
  public void testIO() throws Exception {
    createJsons();
    PreviewJsonCombiner combiner = PreviewJsonCombiner.builder()
      .directory(dir)
      .input(A_JSON)
      .input(B_JSON)
      .output(COMBINED_JSON)
      .build();
    List<Map<?, ?>> all = combiner.loadAll();
    assertThat(all).contains(A1, A2, B1);
    combiner.writeCombined(all);
    assertThat(readCombined()).contains(A1, A2, B1);
  }

  @Test
  public void testLimit() throws IOException {
    createJsons();
    PreviewJsonCombiner combiner = PreviewJsonCombiner.builder()
      .directory(dir)
      .input(A_JSON)
      .input(B_JSON)
      .output(COMBINED_JSON)
      .limit(2)
      .build();
    combiner.writeCombined();
    List<Object> combined = readCombined();
    assertThat(combined.size()).isEqualTo(2);
  }
  
  
  private List<Object> readCombined() throws IOException, JsonParseException, JsonMappingException {
    return Arrays.asList(MAPPER.readValue(dir.resolve(COMBINED_JSON).toFile(), Map[].class));
  }

  private void createJsons() throws JsonGenerationException, JsonMappingException, IOException {
    Object[] arr1 = {A1, A2};
    Object[] arr2 = {B1};
    MAPPER.writeValue(dir.resolve(A_JSON).toFile(), arr1);
    MAPPER.writeValue(dir.resolve(B_JSON).toFile(), arr2);
  }

}
