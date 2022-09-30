package io.github.redouane59.dzdialect.datasetbuilder.sentence.generators;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence.SentenceContent;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PVSentenceGenerator extends AbstractSentenceGenerator {

  @Override
  public List<Sentence> generateAllSentences() {
    List<Sentence> result = new ArrayList<>();
    for (Tense tense : Tense.values()) {
      for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
        List<Translation> translations = new ArrayList<>();
        Optional<Conjugation> etre = DB.AUX_ETRE.getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(),
                                                                                                  pronoun.isSingular(),
                                                                                                  pronoun.getPossession(),
                                                                                                  tense);
        if (etre.isPresent()) {
          String frSentence = pronoun.getTranslationValue(Lang.FR)
                              + " "
                              + etre.get().getTranslationValue(Lang.FR);
          String dzSentence   = etre.get().getTranslationValue(Lang.DZ);
          String dzArSentence = etre.get().getTranslationValueAr(Lang.DZ);
          translations.add(new Translation(Lang.FR, frSentence));
          translations.add(new Translation(Lang.DZ, dzSentence, dzArSentence));
          Sentence sentence = new Sentence(translations);
          sentence.setContent(SentenceContent.builder().pronouns(List.of(pronoun)).build());
          //     sentence.setRandomWords(generateRandomAlternativeWords(sentence.getContent()));
          result.add(sentence);
        } else {
          System.out.println("verb not found with tense : " + tense.getId());
        }
      }
    }
    return result;
  }

  // @todo
  @Override
  public Map<Lang, List<String>> generateRandomAlternativeWords(SentenceContent content) {
    Map<Lang, List<String>> result = new HashMap<>();
    return result;
  }

}
