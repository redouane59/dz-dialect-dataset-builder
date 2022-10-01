package io.github.redouane59.dzdialect.databuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AdjectivesTest {

  @BeforeAll
  public static void init() throws IOException {
    DB.init();
  }

  @Test
  public void testDeserializeAdjectives() {
    assertTrue(DB.ADJECTIVES.size() > 30);
    System.out.println(DB.ADJECTIVES.size() + " adjectives found");
  }

  @Test
  public void testConfigurations() {
    int missingConfig = 0;
    for (Adjective adjective : DB.ADJECTIVES) {
      if (!adjective.isDefinitive() && !adjective.isTemporal()) {
        missingConfig++;
        System.out.println("no config found for verb : " + adjective.getId());
      }
    }

    assertEquals(0, missingConfig);
  }

  @Test
  public void printAdjectives() {
    DB.ADJECTIVES.stream().map(Adjective::getValues).forEach(System.out::println);
  }


}
