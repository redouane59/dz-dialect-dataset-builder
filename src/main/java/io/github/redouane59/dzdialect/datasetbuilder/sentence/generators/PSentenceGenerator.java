package io.github.redouane59.dzdialect.datasetbuilder.sentence.generators;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence.SentenceContent;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.SentenceSchema;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PSentenceGenerator extends AbstractSentenceGenerator {

  public static PossessiveWord getRandomPronoun() {
    return DB.PERSONAL_PRONOUNS.getValues()
                               .stream()
                               .skip(RANDOM.nextInt(DB.PERSONAL_PRONOUNS.getValues().size()))
                               .findFirst()
                               .get();
  }

  public static PossessiveWord getRandomPronounWithOneSimilitude(PossessiveWord other) {
    List<PossessiveWord> pronouns = new ArrayList<>(DB.PERSONAL_PRONOUNS.getValues());
    Collections.shuffle(pronouns);
    return pronouns.stream()
                   .filter(o -> o.getPossession() == other.getPossession()
                                || o.isSingular() == other.isSingular()
                           //   || o.getGender() == other.getGender()
                   )
                   .findFirst()
                   .get();
  }

  @Override
  public List<Sentence> generateAllSentences() {
    List<Sentence> result = new ArrayList<>();
    for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
      Sentence sentence = new Sentence(pronoun.getTranslations());
      sentence.setContent(SentenceContent.builder().pronouns(List.of(pronoun)).build());
      sentence.setRandomWords(generateRandomAlternativeWords(sentence.getContent()));
      sentence.setSentenceSchema(SentenceSchema.P);
      result.add(sentence);
    }
    return result;
  }

  @Override
  public Map<Lang, List<String>> generateRandomAlternativeWords(SentenceContent content) {
    Map<Lang, List<String>> result = new HashMap<>();

    Set<String> frValues = new HashSet<>();
    Set<String> dzValues = new HashSet<>();
    frValues.add(content.getPronouns().get(0).getTranslationValue(Lang.FR));
    dzValues.add(content.getPronouns().get(0).getTranslationValue(Lang.DZ));

    int i = 0;
    while (i < getParameters().getAlternativeCount()) {
      PossessiveWord alternative = getRandomPronoun();
      String         dzWord      = alternative.getTranslationValue(Lang.DZ);
      String         frWord      = alternative.getTranslationValue(Lang.FR);
      if (!content.getPronouns().stream().map(p -> p.getTranslationValue(Lang.DZ)).toList().contains(dzWord)
          && !dzValues.contains(dzWord)) {
        dzValues.add(dzWord);
        frValues.add(frWord);
        i++;
      }
    }
    result.put(Lang.FR, new ArrayList<>(frValues));
    result.put(Lang.DZ, new ArrayList<>(dzValues));
    return result;
  }

}
