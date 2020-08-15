package de.jlab.scales;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UtilsTest {


  @Test
  public void testUuid() {
    final int N = 10000;
    Set<String> set = new HashSet<>();
    for (int i = 0; i < N; i++) {
      set.add(Utils.uuid());
    }
    assertEquals(N, set.size());
    
  }

}
