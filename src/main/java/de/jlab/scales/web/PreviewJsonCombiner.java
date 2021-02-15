package de.jlab.scales.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@lombok.Builder
public class PreviewJsonCombiner {
  @lombok.Singular
  private List<String> inputs;
  private int limit;
  private Path directory;
  private String output;
  
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private PreviewJsonCombiner(List<String> inputs, int limit, Path directory, String output) {
    this.inputs = inputs;
    this.limit = limit;
    this.directory = directory;
    this.output = output;
  }
  
  public List<Map<?,?>> loadAll() throws IOException {
    List<Map<?,?>> result = new ArrayList<>();
    for (String input : inputs) {
      File file = directory.resolve(input).toFile();
      Map<?,?>[] maps = MAPPER.readValue(file, Map[].class);
      result.addAll(Arrays.asList(maps));
    }
    return result;
  }

  public void writeCombined(List<Map<?, ?>> list) throws IOException {
    File file = directory.resolve(output).toFile();
    MAPPER.writeValue(file, list);
  }

  public void writeCombined() throws IOException {
    List<Map<?,?>> all = loadAll();
    Collections.shuffle(all);
    List<Map<?, ?>> limited = all.subList(0, limit);
    writeCombined(limited);
  }

}
