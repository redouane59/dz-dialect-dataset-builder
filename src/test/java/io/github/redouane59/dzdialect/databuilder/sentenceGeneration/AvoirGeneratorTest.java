package io.github.redouane59.dzdialect.databuilder.sentenceGeneration;

import static io.github.redouane59.dzdialect.datasetbuilder.helper.PrintHelper.printSentence;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AvoirGeneratorTest {

  private final PVSentenceGenerator generator = new PVSentenceGenerator();
  private final Verb                verb      = DB.AUX_AVOIR;
  private final Map<String, String> numbers   = Map.of(
      "zoudj", "deux",
      "telt", "trois",
      "rab3a", "quatre",
      "khams", "cinq",
      "sett", "six",
      "seb3a", "sept",
      "tmen", "huit",
      "tess3a", "neuf",
      "3achra", "dix"
  );

  @BeforeAll
  public static void init() throws IOException {
    DB.init();
  }

  @Test
  public void generateChildSentences() {

    Map<String, String> words = Map.of(
        "tefla", "une fille",
        "wled", "un garçon"
    );

    List<Tense> tenses = List.of(Tense.PRESENT);

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
        for (Entry<String, String> word : words.entrySet()) {
          printSentence(sentenceDTO.getDz() + " " + word.getKey(),
                        sentenceDTO.getFr() + " " + word.getValue());
        }
      }
    }
  }

  @Test
  public void generateChildrenSentences() {

    List<Word> words = List.of(
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("filles")).findFirst().get(),
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("fils")).findFirst().get(),
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("enfants")).findFirst().get()
    );

    List<Tense> tenses = List.of(Tense.PRESENT);

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
        for (Word word : words) {
          for (Entry<String, String> number : numbers.entrySet()) {
            printSentence(sentenceDTO.getDz() + " " + number.getKey() + " " + word.getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + " " + number.getValue() + " " + word.getTranslationValue(Lang.FR));
          }
        }
      }
    }
  }

  @Test
  public void generateSiblingsSentences() {

    List<Word> siblings = List.of(
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("frères")).findFirst().get(),
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("soeurs")).findFirst().get()
    );

    List<Tense> tenses = List.of(Tense.PRESENT);
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        for (Word sibling : siblings) {
          for (Entry<String, String> number : numbers.entrySet()) {
            SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
            printSentence(sentenceDTO.getDz() + " " + number.getKey() + " " + sibling.getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + " " + number.getValue() + " " + sibling.getTranslationValue(Lang.FR));
          }
        }
      }
    }
  }

  @Test
  public void generateBrotherSentences() {

    List<Word> words = List.of(
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("frère")).findFirst().get()
    );

    List<Tense> tenses = List.of(Tense.PRESENT);
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      for (Tense tense : tenses) {
        for (Word w : words) {
          Optional<PossessiveWord> dzEndingOpt = DB.BROTHER_ENDING.stream().filter(
              p -> p.isSingular() == pronoun.isSingular()
                   && p.getPossession() == pronoun.getPossession()
                   && (p.getGender() == pronoun.getGender() || p.getGender() == Gender.X)
          ).findFirst();
          if (dzEndingOpt.isPresent()) {
            SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
            printSentence(sentenceDTO.getDz() + " " + w.getTranslationValue(Lang.DZ) + dzEndingOpt.get().getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + " un " + w.getTranslationValue(Lang.FR));
          } else {
            System.err.println("not present");
          }
        }
      }
    }
  }

  @Test
  public void generateSisterSentences() {

    List<GenderedWord> words = List.of(
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("soeur")).findFirst().get(),
        DB.PERSONS.stream().filter(p -> p.getTranslationValue(Lang.FR).equals("voisin")).findFirst().get()
    );

    List<Tense> tenses = List.of(Tense.PRESENT);
    for (GenderedWord w : words) {
      String frenchArticle = " un ";
      if (w.getGender() == Gender.F) {
        frenchArticle = " une ";
      }
      for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
        for (Tense tense : tenses) {
          Optional<PossessiveWord> dzEndingOpt = DB.SISTER_ENDING.stream().filter(
              p -> p.isSingular() == pronoun.isSingular()
                   && p.getPossession() == pronoun.getPossession()
                   && (p.getGender() == pronoun.getGender() || p.getGender() == Gender.X)
          ).findFirst();
          if (dzEndingOpt.isPresent()) {
            SentenceDTO sentenceDTO = new SentenceDTO(generator.getSentence(verb, pronoun, tense));
            printSentence(sentenceDTO.getDz() + " " + w.getTranslationValue(Lang.DZ) + dzEndingOpt.get().getTranslationValue(Lang.DZ),
                          sentenceDTO.getFr() + frenchArticle + w.getTranslationValue(Lang.FR));
          } else {
            System.err.println("not present");
          }
        }
      }
    }
  }


}
