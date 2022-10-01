package io.github.redouane59.dzdialect.datasetbuilder.sentence.generators;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.noun.NounType;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence.SentenceContent;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PVASentenceGenerator extends AbstractSentenceGenerator {

  @Override
  public List<Sentence> generateAllSentences() {
    List<Sentence> result = new ArrayList<>();
    for (Adjective adjective : DB.ADJECTIVES) {
      for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
        if (!adjective.getPossibleNouns().contains(NounType.PERSON) || pronoun.getPossession() == Possession.OTHER) {
          List<Translation>      translations = new ArrayList<>();
          Optional<GenderedWord> adjValueFr   = adjective.getWordByGenderAndSingular(pronoun.getGender(), Lang.FR, pronoun.isSingular());
          Optional<GenderedWord> adjValueDz   = adjective.getWordByGenderAndSingular(pronoun.getGender(), Lang.FR, pronoun.isSingular());

          String frSentence = pronoun.getTranslationValue(Lang.FR) + " "
                              + adjective.getAuxiliarFromAdjective(Lang.FR)
                                         .getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(Lang.FR),
                                                                                           pronoun.isSingular(),
                                                                                           pronoun.getPossession(),
                                                                                           Tense.PRESENT)
                                         .get()
                                         .getTranslationValue(Lang.FR)
                              + " " + adjValueFr.get().getTranslationValue(Lang.FR);
          String dzSentence;
          String dzArSentence;
          if (adjective.isTemporal()) {
            dzSentence   = DB.AUX_ETRE.getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(Lang.DZ),
                                                                                        pronoun.isSingular(),
                                                                                        pronoun.getPossession(),
                                                                                        Tense.PRESENT).get().getTranslationValue(Lang.DZ)
                           + " " + adjValueDz.get().getTranslationValue(Lang.FR);
            dzArSentence = DB.AUX_ETRE.getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(Lang.DZ),
                                                                                        pronoun.isSingular(),
                                                                                        pronoun.getPossession(),
                                                                                        Tense.PRESENT).get().getTranslationValueAr(Lang.DZ)
                           + " " + adjValueDz.get().getTranslationValueAr(Lang.FR);
          } else {
            dzSentence   = pronoun.getTranslationValue(Lang.DZ) + " " + adjValueDz.get().getTranslationValue(Lang.DZ);
            dzArSentence = pronoun.getTranslationValueAr(Lang.DZ) + " " + adjValueDz.get().getTranslationValueAr(Lang.DZ);
          }

          translations.add(new Translation(Lang.FR, frSentence));
          translations.add(new Translation(Lang.DZ, dzSentence, dzArSentence));
          Sentence sentence = new Sentence(translations);
          sentence.setContent(SentenceContent.builder()
                                             .pronouns(List.of(pronoun))
                                             .subtense(Tense.PRESENT.getId())
                                             .adjectives(List.of(adjective))
                                             .build());
          //     sentence.setRandomWords(generateRandomAlternativeWords(sentence.getContent()));
          result.add(sentence);
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
