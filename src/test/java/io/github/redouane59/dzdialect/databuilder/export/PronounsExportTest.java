package io.github.redouane59.dzdialect.databuilder.export;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PronounsExportTest {

  @BeforeAll
  public static void inti() throws IOException {
    DB.init();
  }

  @Test
  public void exportPronouns() throws JsonProcessingException {
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      Sentence    sentence    = new Sentence(pronoun.getTranslations());
      SentenceDTO sentenceDTO = new SentenceDTO(sentence);
      System.out.println(OBJECT_MAPPER.writeValueAsString(sentenceDTO));
    }
  }
}
