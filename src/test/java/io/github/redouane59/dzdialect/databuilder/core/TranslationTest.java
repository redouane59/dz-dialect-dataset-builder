package io.github.redouane59.dzdialect.databuilder.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import org.junit.jupiter.api.Test;

public class TranslationTest {

  @Test
  public void testCleanValue() {
    assertEquals("j'aime", Translation.cleanValue("je aime", Lang.FR));
    assertEquals("l'arbre", Translation.cleanValue("le arbre", Lang.FR));
    assertEquals("n'imagine", Translation.cleanValue("ne imagine", Lang.FR));
  }
}
