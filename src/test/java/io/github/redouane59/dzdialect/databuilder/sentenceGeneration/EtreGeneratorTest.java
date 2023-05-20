package io.github.redouane59.dzdialect.databuilder.sentenceGeneration;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Location;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
        for (PossessiveWord w : with) {
          if (pronoun.getPossession() != w.getPossession() || pronoun.isSingular() != w.isSingular()) {
            printSentence(sentenceDTO.getDz() + " " + w.getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + " " + w.getTranslationValue(Lang.FR));
          }
        }
      }
    }
  }

  @Test
  public void generateLocationSentences() {

    List<Tense> tenses = List.of(Tense.PRESENT, Tense.PAST);

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
        for (Location w : DB.LOCATION) {
          printSentence(sentenceDTO.getDz() + " " + w.getAt().getTranslationValue(Lang.DZ),
                        sentenceDTO.getFr() + " " + w.getAt().getTranslationValue(Lang.FR));
        }
      }
    }
  }

  public void printSentence(String dz, String fr) {
    System.out.println(dz + "," + fr);
  }


  public void printGenderInfo(PossessiveWord pronoun) {
    if (pronoun.getGender().equals(Gender.F)) {
      System.out.println(" (dit à une femme)");
    } else if (pronoun.getGender().equals(Gender.M)) {
      System.out.println(" (dit à un homme)");
    } else {
      System.out.println();
    }
  }


}
