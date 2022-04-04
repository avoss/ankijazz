package com.ankijazz.anki;

import java.nio.file.Path;
import java.util.LinkedHashMap;
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
    return fields.values().stream().map(s -> s.replace(CSV_DELIMITER, " ")).collect(Collectors.joining(CSV_DELIMITER));
  }

  @Override
  public Map<String, Object> getJson() {
    return new LinkedHashMap<String, Object>(fields);
  }
  
  @Override
  public void writeAssets(Path directory) {
    // no assets
  }
  
  @Override
  public String getAssetId() {
    return SimpleCard.class.getName();
  }
  
  public Map<String, String> getFields() {
    return fields;
  }
}
