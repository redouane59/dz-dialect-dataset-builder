package io.github.redouane59.dzdialect.databuilder.out;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO.SentenceDTO;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.generators.PVSentenceGenerator;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
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
  public void generateAllPVSentences() {
    for (Verb verb : DB.VERBS) {
      for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
        Sentence sentence = generator.getSentence(verb, pronoun, Tense.IMPERATIVE);
        if (sentence != null) {
          SentenceDTO sentenceDTO = new SentenceDTO(sentence);
          if (verb.isDirectComplement() && !verb.isDzOppositeComplement()) {
            // iou -> ih , ouou -> ou
            System.out.print((sentenceDTO.getDz() + "ou,").replace("iou", "ih")
                                                          .replace("ouou", "ouh") + sentenceDTO.getFr() + "-le");
            printGenderInfo(pronoun);
            System.out.print(sentenceDTO.getDz() + "ha," + sentenceDTO.getFr() + "-la");
            printGenderInfo(pronoun);
            System.out.print(sentenceDTO.getDz() + "houm," + sentenceDTO.getFr() + "-les");
            printGenderInfo(pronoun);
          }
          if (verb.isIndirectComplement() || verb.isDzOppositeComplement()) {
            String dzComplent = "";
            String frComplent = "";
            if (!verb.isDzOppositeComplement()) {
              dzComplent = " 7aja";
              frComplent = " quelque chose";
            }
            System.out.print(sentenceDTO.getDz() + "li" + dzComplent + "," + sentenceDTO.getFr() + "-moi" + frComplent);
            printGenderInfo(pronoun);
            System.out.print((sentenceDTO.getDz() + "lou").replace("liou", "lih").replace("louou", "louh")
                             + dzComplent
                             + ","
                             + sentenceDTO.getFr()
                             + "-lui (h)"
                             + frComplent);
            printGenderInfo(pronoun);
            System.out.print(sentenceDTO.getDz() + "lha" + dzComplent + "," + sentenceDTO.getFr() + "-lui (f)" + frComplent);
            printGenderInfo(pronoun);
            System.out.print(sentenceDTO.getDz() + "lna" + dzComplent + "," + sentenceDTO.getFr() + "-nous" + frComplent);
            printGenderInfo(pronoun);
            System.out.print(sentenceDTO.getDz() + "lhoum" + dzComplent + "," + sentenceDTO.getFr() + "-leur" + frComplent);
            printGenderInfo(pronoun);
          }
        }
      }
    }
  }

  @Test
  public void generateOneVerbPVSentences() {
    Map<String, String> with = Map.of("m3aya", "avec moi",
                                      "m3ak", "avec toi",
                                      "m3ah", "avec lui",
                                      "m3aha", "avec elle",
                                      "m3ana", "avec nous",
                                      "m3akoum", "avec vous",
                                      "m3ahoum", "avec eux");

    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      Sentence    sentence    = generator.getSentence(DB.AUX_ETRE, pronoun, Tense.PAST);
      SentenceDTO sentenceDTO = new SentenceDTO(sentence);
      for (Entry<String, String> x : with.entrySet()) {
        System.out.println(sentenceDTO.getDz() + " " + x.getKey() + ","
                           + sentenceDTO.getFr() + " " + x.getValue());
      }
    }
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
