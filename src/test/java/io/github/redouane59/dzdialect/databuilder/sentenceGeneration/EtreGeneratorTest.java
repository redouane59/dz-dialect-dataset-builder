package io.github.redouane59.dzdialect.databuilder.sentenceGeneration;

import static io.github.redouane59.dzdialect.datasetbuilder.helper.PrintHelper.printSentence;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.noun.NounType;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Location;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.TimeExpression;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EtreGeneratorTest {

  PVSentenceGenerator generator = new PVSentenceGenerator();
  Verb                verb      = DB.AUX_ETRE;

  @BeforeAll
  public static void init() throws IOException {
    DB.init();
  }


  @Test
  public void generateLocationSentences() {

    List<Tense>         tenses              = List.of(Tense.PRESENT, Tense.PAST);
    Set<TimeExpression> pastTimeExpressions = DB.TIMES_EXPRESSIONS.stream().filter(t -> t.getTense() == Tense.PAST).collect(Collectors.toSet());

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
        for (Location w : DB.LOCATIONS) {
          String dzSentence = sentenceDTO.getDz() + " " + w.getAt().getTranslationValue(Lang.DZ);
          String frSentence = sentenceDTO.getFr() + " " + w.getAt().getTranslationValue(Lang.FR);
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

  @Test
  public void generateAdjectiveSentencesEtre() {
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, Tense.PRESENT));
      Set<Adjective>
          adjectives =
          DB.ADJECTIVES.stream().filter(Adjective::isTemporal).filter(w -> !w.isFrAuxiliarAvoir()).collect(Collectors.toSet());
      for (Adjective w : adjectives) {
        Optional<GenderedWord> adjectiveOpt = w.getWordByGenderAndSingular(pronoun.getGender(), Lang.DZ, pronoun.isSingular());
        if (pronoun.getPossession() == Possession.OTHER || w.getPossibleNouns().contains(NounType.PERSON)) {

          adjectiveOpt.ifPresent(adjective -> printSentence(sentenceDTO.getDz() + " " + adjective
                                                                .getTranslationValue(Lang.DZ),
                                                            sentenceDTO.getFr() + " " + adjective
                                                                .getTranslationValue(Lang.FR)));
        }
      }
    }
  }

  @Test
  public void generateAdjectiveSentencesAvoir() {
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(DB.AUX_AVOIR, pronoun, Tense.PRESENT));
      Set<Adjective>
          adjectives =
          DB.ADJECTIVES.stream().filter(Adjective::isTemporal).filter(Adjective::isFrAuxiliarAvoir).collect(Collectors.toSet());
      for (Adjective w : adjectives) {
        Optional<GenderedWord> adjectiveOpt = w.getWordByGenderAndSingular(pronoun.getGender(), Lang.DZ, pronoun.isSingular());
        if (pronoun.getPossession() == Possession.OTHER || w.getPossibleNouns().contains(NounType.PERSON)) {

          adjectiveOpt.ifPresent(adjective -> printSentence(sentenceDTO.getDz() + " " + adjective
                                                                .getTranslationValue(Lang.DZ),
                                                            sentenceDTO.getFr() + " " + adjective
                                                                .getTranslationValue(Lang.FR)));
        }
      }
    }
  }


}
