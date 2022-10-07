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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PVASentenceGenerator extends AbstractSentenceGenerator {

  public static Adjective getRandomAdjective() {
    return DB.ADJECTIVES.stream().skip(RANDOM.nextInt(DB.ADJECTIVES.size())).findFirst().get();
  }

  @Override
  public List<Sentence> generateAllSentences() {
    List<Sentence> result = new ArrayList<>();
    for (Adjective adjective : DB.ADJECTIVES) {
      for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
        if (adjective.getPossibleNouns().contains(NounType.PERSON) || pronoun.getPossession() == Possession.OTHER) {
          List<Translation>      translations = new ArrayList<>();
          Optional<GenderedWord> adjValueFr   = adjective.getWordByGenderAndSingular(pronoun.getGender(), Lang.FR, pronoun.isSingular());
          Optional<GenderedWord> adjValueDz   = adjective.getWordByGenderAndSingular(pronoun.getGender(), Lang.DZ, pronoun.isSingular());

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
                           + " " + adjValueDz.get().getTranslationValue(Lang.DZ);
            dzArSentence = DB.AUX_ETRE.getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(Lang.DZ),
                                                                                        pronoun.isSingular(),
                                                                                        pronoun.getPossession(),
                                                                                        Tense.PRESENT).get().getTranslationValueAr(Lang.DZ)
                           + " " + adjValueDz.get().getTranslationValueAr(Lang.DZ);
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
          sentence.setRandomWords(generateRandomAlternativeWords(sentence.getContent()));
          sentence.getRandomWords().get(Lang.FR).addAll(List.of(frSentence.split(" ")));
          sentence.getRandomWords().get(Lang.DZ).addAll(List.of(dzSentence.split(" ")));
          result.add(sentence);
        }
      }
    }
    return result;
  }

  // @todo
  @Override
  public Map<Lang, List<String>> generateRandomAlternativeWords(SentenceContent content) {
    Map<Lang, List<String>> result   = new HashMap<>();
    PossessiveWord          pronoun  = content.getPronouns().get(0);
    Set<String>             frValues = new HashSet<>();
    Set<String>             dzValues = new HashSet<>();

    int i = 0;
    while (i < getParameters().getAlternativeCount()) {
      Adjective adjective = getRandomAdjective();
      if (!content.getAdjectives().contains(adjective)) {

        String
            dzWord =
            adjective.getWordByGenderAndSingular(pronoun.getGender(Lang.DZ), Lang.DZ, pronoun.isSingular()).get().getTranslationValue(Lang.DZ);
        String
            frWord =
            adjective.getWordByGenderAndSingular(pronoun.getGender(Lang.FR), Lang.FR, pronoun.isSingular()).get().getTranslationValue(Lang.FR);
        if (!dzValues.contains(dzWord)) {
          dzValues.add(dzWord);
          frValues.add(frWord);
          i++;
        }
      }

    }
    result.put(Lang.FR, new ArrayList<>(frValues));
    result.put(Lang.DZ, new ArrayList<>(dzValues));
    return result;
  }

}
