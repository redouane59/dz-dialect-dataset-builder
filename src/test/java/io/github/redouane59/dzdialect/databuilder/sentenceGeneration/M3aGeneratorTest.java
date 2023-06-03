package io.github.redouane59.dzdialect.databuilder.sentenceGeneration;

import static io.github.redouane59.dzdialect.datasetbuilder.helper.PrintHelper.printSentence;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.TimeExpression;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class M3aGeneratorTest {

  PVSentenceGenerator generator           = new PVSentenceGenerator();
  Set<TimeExpression> pastTimeExpressions = DB.TIMES_EXPRESSIONS.stream().filter(t -> t.getTense() == Tense.PAST).collect(Collectors.toSet());

  List<Verb> verbs = List.of(
      DB.AUX_ETRE, DB.VERBS.stream().filter(v -> v.getId().equals("aller")).findFirst().get(),
      DB.AUX_ETRE, DB.VERBS.stream().filter(v -> v.getId().equals("parler")).findFirst().get(),
      DB.AUX_ETRE, DB.VERBS.stream().filter(v -> v.getId().equals("dormir")).findFirst().get(),
      DB.AUX_ETRE, DB.VERBS.stream().filter(v -> v.getId().equals("manger")).findFirst().get(),
      DB.AUX_ETRE, DB.VERBS.stream().filter(v -> v.getId().equals("venir")).findFirst().get()
  );

  @BeforeAll
  public static void init() throws IOException {
    DB.init();
  }

  @Test
  public void generateM3aSentences() {
    Set<PossessiveWord> with = Set.of(
        new PossessiveWord("m3aya", "avec moi", Gender.X, true, Possession.I),
        new PossessiveWord("m3ak", "avec toi", Gender.X, true, Possession.YOU),
        new PossessiveWord("m3ah", "avec lui", Gender.M, true, Possession.OTHER),
        new PossessiveWord("m3aha", "avec elle", Gender.F, true, Possession.OTHER),
        new PossessiveWord("m3a Redouane", "avec Redouane", Gender.F, true, Possession.OTHER),
        new PossessiveWord("m3ana", "avec nous", Gender.X, false, Possession.I),
        new PossessiveWord("m3akoum", "avec vous", Gender.X, false, Possession.YOU),
        new PossessiveWord("m3ahoum", "avec eux", Gender.X, false, Possession.OTHER),
        new PossessiveWord("m3a Redouane wa Ibtissam", "avec Redouane et Ibtissam", Gender.X, false, Possession.OTHER)
    );

    List<Tense> tenses = List.of(Tense.PRESENT, Tense.PAST);
    for (Verb verb : verbs) {
      for (Tense tense : tenses) {
        for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
          Sentence sentence = generator.getSentence(verb, pronoun, tense);
          if (sentence != null) {
            SentenceDTO sentenceDTO = new SentenceDTO(sentence);
            for (PossessiveWord w : with) {
              if ((pronoun.getPossession() != w.getPossession()) || pronoun.getPossession() == Possession.OTHER) {
                String dzSentence = sentenceDTO.getDz() + " " + w.getTranslationValue(Lang.DZ);
                String frSentence = sentenceDTO.getFr() + " " + w.getTranslationValue(Lang.FR);
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
          } else {
            System.err.println("no sentence generated for " + verb.getId() + " " + pronoun.getId() + " " + tense.getId());
          }
        }
      }
    }
  }

  @Test
  public void generateM3aSiblingSentences() {
    Word brother = DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("frère")).findFirst().get();
    Word sister  = DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("soeur")).findFirst().get();

    List<Tense> tenses = List.of(Tense.PRESENT, Tense.PAST);
    String      withDz = " m3a ";
    String      withFr = " avec ";
    for (Verb verb : verbs) {
      for (Tense tense : tenses) {
        for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
          for (Word ending : DB.BROTHER_ENDING) {
            SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
            String      dzSentence  = sentenceDTO.getDz() + withDz + brother.getTranslationValue(Lang.DZ) + ending.getTranslationValue(Lang.DZ);
            String frSentence = sentenceDTO.getFr() + withFr + ending.getTranslationValue(Lang.FR) + " " + brother.getTranslationValue(
                Lang.FR);
            if (tense == Tense.PAST) {
              for (TimeExpression timeExpression : pastTimeExpressions) {
                printSentence(timeExpression.getTranslationValue(Lang.DZ) + " " + dzSentence,
                              timeExpression.getTranslationValue(Lang.FR) + " " + frSentence);
              }
            } else {
              printSentence(dzSentence, frSentence);
            }

          }
          for (Word ending : DB.SISTER_ENDING) {
            SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
            printSentence(sentenceDTO.getDz() + withDz + sister.getTranslationValue(Lang.DZ) + ending.getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + withFr + ending.getTranslationValue(Lang.FR) + " " + sister.getTranslationValue(
                              Lang.FR));
          }
        }
      }
    }
  }


}
