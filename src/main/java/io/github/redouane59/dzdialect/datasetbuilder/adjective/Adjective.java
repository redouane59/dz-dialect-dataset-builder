package io.github.redouane59.dzdialect.datasetbuilder.adjective;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.WordType;
import io.github.redouane59.dzdialect.datasetbuilder.noun.NounType;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.ConjugationListDeserializer;
import io.github.redouane59.dzdialect.datasetbuilder.word.abstracts.AbstractWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;

@Getter
public class Adjective extends AbstractWord {

  @JsonInclude(Include.NON_EMPTY)
  @JsonDeserialize(using = ConjugationListDeserializer.class)
  private final List<GenderedWord> values = new ArrayList<>();
  @JsonProperty("possible_nouns")
  List<NounType> possibleNouns = new ArrayList<>();
  private boolean temporal;
  private boolean definitive;
  @JsonProperty("fr_aux_avoir")
  private boolean frAuxiliarAvoir;

  public Adjective() {
    setWordType(WordType.ADJECTIVE);
  }

  public static Set<Adjective> deserializeFromList(List<List<String>> entries) {
    int adjectiveIdIndex = 0;
    int singularIndex    = 1;
    int genderIndex      = 2;
    int frValueIndex     = 3;
    int dzValueIndex     = 4;
    int dzValueArIndex   = 5;
    // int shouldBeImportIndex = 6;
    Set<Adjective> adjectives = new HashSet<>();

    for (List<String> values : entries) {
      if (values.size() >= 6) {
        Adjective           abstractAdjective = new Adjective();
        Optional<Adjective> adjectiveOpt      = adjectives.stream().filter(o -> o.getId().equals(values.get(adjectiveIdIndex))).findFirst();
        if (adjectiveOpt.isEmpty()) { // new adjective
          abstractAdjective.setId(values.get(adjectiveIdIndex));
          adjectives.add(abstractAdjective);
        } else { // existing adjective
          abstractAdjective = adjectiveOpt.get();
        }

        try {

          boolean singular  = Boolean.parseBoolean(values.get(singularIndex));
          Gender  gender    = Gender.valueOf(values.get(genderIndex));
          String  frValue   = values.get(frValueIndex);
          String  dzValue   = values.get(dzValueIndex);
          String  dzValueAr = null;
          if (values.size() > dzValueArIndex) {
            dzValueAr = values.get(dzValueArIndex);
          }

          Conjugation adjective = new Conjugation();
          adjective.setSingular(singular);
          adjective.setGender(gender);
          adjective.setTranslations(List.of(new Translation(Lang.FR, frValue),
                                            new Translation(Lang.DZ, dzValue, dzValueAr)));
          abstractAdjective.getValues().add(adjective);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        System.err.println("adjective ignored because not enough values : " + values.get(0));
      }
    }

    return adjectives;
  }

  public static Optional<GenderedWord> getValue(Adjective adjective, PossessiveWord subject, Lang lang) {
    if (subject == null) {
      System.out.println("null subject");
      return Optional.empty();
    }
    return adjective.getWordByGenderAndSingular(subject.getGender(lang), lang, subject.isSingular());
  }

  public Verb getAuxiliarFromAdjective(Lang lang) {
    if (lang == Lang.FR && isFrAuxiliarAvoir()) {
      return DB.AUX_AVOIR;
    }
    return DB.AUX_ETRE;
  }

  public Conjugation getAuxiliarConjugationFromAdjective(GenderedWord adjective, Lang lang, Possession possession, Tense tense) {
    return getAuxiliarFromAdjective(lang).getConjugationByGenderSingularPossessionAndTense(adjective.getGender(lang),
                                                                                           adjective.isSingular(),
                                                                                           possession,
                                                                                           tense).get();
  }

  /**
   * Used after importing adjectives without configuration
   *
   * @param adjective the adjective from which to take the parameters to copy
   */
  public void importConfig(final Adjective adjective) {
    this.possibleNouns   = adjective.getPossibleNouns();
    this.temporal        = adjective.isTemporal();
    this.definitive      = adjective.isDefinitive();
    this.frAuxiliarAvoir = adjective.isFrAuxiliarAvoir();
  }
}