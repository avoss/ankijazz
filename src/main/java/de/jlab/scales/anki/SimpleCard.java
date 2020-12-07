package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleCard implements Card {
  private final double difficulty;
  private final Map<String, String> fields = new LinkedHashMap<>();
  private final Set<String> fieldNames;


  public SimpleCard(double difficulty, String ... fieldNames) {
    this.difficulty = difficulty;
    this.fieldNames = Set.of(fieldNames);
    for (String fieldName : fieldNames) {
      fields.put(fieldName, "");
    }
  }

  @Override
  public double getDifficulty() {
    return difficulty;
  }
  
  public void put(String name, String value) {
    if (!fieldNames.contains(name)) {
      throw new IllegalArgumentException("unknown field: " + name);
    }
    fields.put(name, value);
  }

  @Override
  public String getCsv() {
    return fields.values().stream().map(v -> v.replace(';', '_')).collect(Collectors.joining(";"));
  }
  
  public Map<String, String> getFields() {
    return fields;
  }
  

  @Override
  public void writeAssets(Path directory) {
  }
}
