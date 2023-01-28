package io.github.redouane59.dzdialect.datasetbuilder.sentence.generators;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence.SentenceContent;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.SentenceSchema;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PVSentenceGenerator extends AbstractSentenceGenerator {

  @Override
  public List<Sentence> generateAllSentences() {
    List<Sentence> result = new ArrayList<>();
    for (Verb verb : DB.VERBS) {
      for (Tense tense : Tense.values()) {
        for (PossessiveWord pronoun : DB.PERSONAL_PRONOUNS.getValues()) {
          result.add(getSentence(verb, pronoun, tense));
        }
      }
    }
    return result;
  }

  public Sentence getSentence(Verb verb, PossessiveWord pronoun, Tense tense) {

    List<Translation> translations = new ArrayList<>();
    Optional<Conjugation> v = verb.getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(),
                                                                                    pronoun.isSingular(),
                                                                                    pronoun.getPossession(),
                                                                                    tense);
    if (v.isPresent()) {
      String frSentence = "";
      if (tense != Tense.IMPERATIVE) {
        frSentence += pronoun.getTranslationValue(Lang.FR) + " ";
      }
      frSentence += v.get().getTranslationValue(Lang.FR);
      String dzSentence   = v.get().getTranslationValue(Lang.DZ);
      String dzArSentence = v.get().getTranslationValueAr(Lang.DZ);
      translations.add(new Translation(Lang.FR, frSentence));
      translations.add(new Translation(Lang.DZ, dzSentence, dzArSentence));
      Sentence sentence = new Sentence(translations);
      sentence.setContent(SentenceContent.builder().pronouns(List.of(pronoun)).verbs(List.of(verb)).subtense(tense.getId()).build());
      sentence.setRandomWords(generateRandomAlternativeWords(sentence.getContent()));
      sentence.setSentenceSchema(SentenceSchema.PV);
      return sentence;
    }
    return null;
  }

  // @todo
  @Override
  public Map<Lang, List<String>> generateRandomAlternativeWords(SentenceContent content) {
    Map<Lang, List<String>> result   = new HashMap<>();
    Set<String>             frValues = new HashSet<>();
    Set<String>             dzValues = new HashSet<>();
    Verb                    verb     = content.getVerbs().get(0);
    PossessiveWord          pronoun  = content.getPronouns().get(0);
    Optional<Conjugation>
        conjugationOpt =
        verb.getConjugationByGenderSingularPossessionAndTense(pronoun.getGender(), pronoun.isSingular(), pronoun.getPossession(), Tense.PRESENT);

    String      dzSentence    = conjugationOpt.get().getTranslationValue(Lang.DZ);
    String      frVerb        = conjugationOpt.get().getTranslationValue(Lang.FR);
    String      frSentence    = pronoun.getTranslationValue(Lang.FR) + " " + frVerb;
    Translation frTranslation = new Translation(Lang.FR, frSentence);
    Translation dzTranslation = new Translation(Lang.DZ, dzSentence);
    Sentence    sentence      = new Sentence(List.of(frTranslation, dzTranslation));
    dzValues.addAll(sentence.getSplittedWords(Lang.DZ));
    frValues.addAll(sentence.getSplittedWords(Lang.FR));

    int i = 0;
    while (i < getParameters().getAlternativeCount()) {
      PossessiveWord randomPronoun = PSentenceGenerator.getRandomPronounWithOneSimilitude(pronoun);
      conjugationOpt =
          verb.getConjugationByGenderSingularPossessionAndTense(randomPronoun.getGender(),
                                                                randomPronoun.isSingular(),
                                                                randomPronoun.getPossession(),
                                                                Tense.PRESENT);
      if (conjugationOpt.isPresent()) {
        dzSentence    = conjugationOpt.get().getTranslationValue(Lang.DZ);
        frVerb        = conjugationOpt.get().getTranslationValue(Lang.FR);
        frSentence    = randomPronoun.getTranslationValue(Lang.FR) + " " + frVerb;
        frTranslation = new Translation(Lang.FR, frSentence);
        dzTranslation = new Translation(Lang.DZ, dzSentence);
        sentence      = new Sentence(List.of(frTranslation, dzTranslation));
        if (!dzValues.contains(dzSentence)) {
          dzValues.addAll(sentence.getSplittedWords(Lang.DZ));
          frValues.addAll(sentence.getSplittedWords(Lang.FR));
          i++;
        }
      }
    }
    result.put(Lang.FR, new ArrayList<>(frValues));
    result.put(Lang.DZ, new ArrayList<>(dzValues));
    return result;
  }

}
