package io.github.redouane59.dzdialect.databuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class VerbsTest {

  @BeforeAll
  public static void init() throws IOException {
    DB.init();
  }

  @Test
  public void testDeserializeVerbs() {
    assertTrue(DB.VERBS.size() > 30);
    System.out.println(DB.VERBS.size() + " verbs found");
    assertNotNull(DB.AUX_ETRE);
    Conjugation etre1SX = DB.AUX_ETRE.getConjugationByGenderSingularPossessionAndTense(Gender.X, true, Possession.I, Tense.PRESENT).get();
    assertEquals("suis", etre1SX.getTranslationValue(Lang.FR));
    assertEquals("rani", etre1SX.getTranslationValue(Lang.DZ));
    Conjugation avoir1SX = DB.AUX_AVOIR.getConjugationByGenderSingularPossessionAndTense(Gender.X, true, Possession.I, Tense.PRESENT).get();
    assertEquals("ai", avoir1SX.getTranslationValue(Lang.FR));
    assertEquals("3ndi", avoir1SX.getTranslationValue(Lang.DZ));
  }

  @Test
  public void testConfigurations() {
    int missingConfig = 0;
    for (Verb verb : DB.VERBS) {
      if (verb.getPossibleQuestionIds().isEmpty() && verb.getVerbType() == null) {
        missingConfig++;
        System.out.println("no config found for verb : " + verb.getId());
      }
    }

    assertEquals(0, missingConfig);
  }

  @Test
  public void printVerbs() {
    DB.VERBS.stream().map(Verb::getValues).forEach(System.out::println);
  }


}
