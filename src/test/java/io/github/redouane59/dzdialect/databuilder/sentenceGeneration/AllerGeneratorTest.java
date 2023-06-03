package io.github.redouane59.dzdialect.databuilder.sentenceGeneration;

import static io.github.redouane59.dzdialect.datasetbuilder.helper.PrintHelper.printSentence;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Location;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.TimeExpression;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AllerGeneratorTest {

  PVSentenceGenerator generator           = new PVSentenceGenerator();
  Verb                verb                = DB.VERBS.stream().filter(v -> v.getId().equals("aller")).findFirst().get();
  Set<TimeExpression> pastTimeExpressions = DB.TIMES_EXPRESSIONS.stream().filter(t -> t.getTense() == Tense.PAST).collect(Collectors.toSet());

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
            String dzSentence = sentenceDTO.getDz() + " " + w.getTo().getTranslationValue(Lang.DZ);
            String frSentence = sentenceDTO.getFr() + " " + w.getTo().getTranslationValue(Lang.FR);
            if (tense == Tense.PAST) {
              for (TimeExpression timeExpression : pastTimeExpressions) {
                printSentence(timeExpression.getTranslationValue(Lang.DZ) + " " + dzSentence,
                              timeExpression.getTranslationValue(Lang.FR) + " " + frSentence);
              }
            } else {
              printSentence(dzSentence, frSentence);
            }

          }
        }
      }
    }
  }

}
