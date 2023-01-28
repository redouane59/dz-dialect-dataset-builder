package io.github.redouane59.dzdialect.databuilder.out;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PVSentenceGeneratorTest {

  PVSentenceGenerator generator = new PVSentenceGenerator();

  @BeforeAll
  public static void inti() throws IOException {
    DB.init();
  }

  @Test
  public void exportSentences() throws JsonProcessingException {
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {

      SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(DB.AUX_AVOIR, pronoun, Tense.PRESENT));
      String      result      = OBJECT_MAPPER.writeValueAsString(sentenceDTO);
      System.out.println(result);
      assertTrue(result.contains("dz"));
      assertTrue(result.contains("dz_ar"));
      assertTrue(result.contains("fr"));
      assertTrue(result.contains("additionnal_information"));
      assertTrue(result.contains("pronouns"));
      assertTrue(result.contains("verbs"));
      assertTrue(result.contains("sentence_schema"));
      assertTrue(result.contains("tense"));
      assertTrue(result.contains("word_propositions")); // @todo to add
    }
  }

  @Test
  public void testWordPropositions() {
/*    int alternativeCount = 4;
    generator.setParameters(GeneratorParameters.builder().alternativeCount(alternativeCount).build());
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      Map<Lang, List<String>> result = generator.generateRandomAlternativeWords(SentenceContent.builder()
                                                                                               .pronouns(List.of(pronoun))
                                                                                               .build());
      assertEquals(alternativeCount, result.get(Lang.FR).size());
      assertEquals(alternativeCount, result.get(Lang.DZ).size());
    }*/
  }
}
