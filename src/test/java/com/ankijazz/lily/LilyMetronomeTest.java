package com.ankijazz.lily;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.ankijazz.lily.LilyMetronome;
import com.ankijazz.lily.LilyMetronome.Tempo;

public class LilyMetronomeTest {

  @Test
  public void test() throws IOException {
    LilyMetronome metronome = new LilyMetronome(5, 50, 75);
    Tempo t50 = metronome.tempo(50);
    assertEquals(t50, metronome.tempo(52));
    assertEquals(t50, metronome.tempo(42));
    Path dir = Paths.get("build/LilyMetronome");
    metronome.writeAssets(dir);
    Path path = dir.resolve(t50.getMp3Name().replace(".mp3",  ".ly"));
    assertThat(path).exists();
    assertThat(Files.readAllLines(path).stream().map(String::trim)).contains("\\tempo 4 = 50");
  }

}
