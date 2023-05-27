package io.github.redouane59.dzdialect.databuilder.sentenceGeneration;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Location;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AllerGeneratorTest {

  PVSentenceGenerator generator = new PVSentenceGenerator();
  Verb                verb      = DB.VERBS.stream().filter(v -> v.getId().equals("aller")).findFirst().get();

  @BeforeAll
  public static void init() throws IOException {
    DB.init();
  }


  @Test
  public void generateLocationSentences() {
    List<Tense> tenses = List.of(Tense.PRESENT, Tense.PAST);

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
        for (Location w : DB.LOCATIONS) {
          if (w.getTo() != null && w.getTo().getTranslationValue(Lang.DZ) != null) {
            printSentence(sentenceDTO.getDz() + " " + w.getTo().getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + " " + w.getTo().getTranslationValue(Lang.FR));
          }
        }
      }
    }
  }

  public void printSentence(String dz, String fr) {
    System.out.println(dz + "," + fr);
  }

}
