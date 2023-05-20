package io.github.redouane59.dzdialect.datasetbuilder.verb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.RootLang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Subtense;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.WordType;
import io.github.redouane59.dzdialect.datasetbuilder.noun.NounType;
import io.github.redouane59.dzdialect.datasetbuilder.word.ConjugationListDeserializer;
import io.github.redouane59.dzdialect.datasetbuilder.word.abstracts.AbstractWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_DEFAULT)
// @todo reflexive verbs (ça me plaît, il me faut, etc.)
public class Verb extends AbstractWord {

  @JsonInclude(Include.NON_EMPTY)
  @JsonDeserialize(using = ConjugationListDeserializer.class)
  private final List<Conjugation> values = new ArrayList<>();

  @JsonProperty("possible_questions")
  private List<String>   possibleQuestionIds = new ArrayList<>();
  @JsonProperty("possible_complements")
  private List<NounType> possibleComplements = new ArrayList<>(); // @todo add verbs for PVV/NVV sentences
  @JsonProperty("indirect_complement")
  private boolean        indirectComplement; // ex: je LUI donne quelque chose.
  @JsonProperty("direct_complement")
  private boolean        directComplement; // ex: je LE donne.
  @JsonProperty("dz_opposite_complement")
  private boolean        dzOppositeComplement; // ex: je l'appelle / n3ayetlou
  @JsonProperty("object_only")
  private boolean        objectOnly; // true : verbe ouvrir
  @JsonProperty("dz_no_suffix")
  private boolean        dzNoSuffix;
  @JsonProperty("semi_auxiliar")
  private boolean        semiAuxiliar; // conjugated verb + infinitive verb
  private WordType       wordType            = WordType.VERB;
  @JsonProperty("verb_type")
  private VerbType       verbType;

  public Optional<Conjugation> getConjugationByGenderSingularPossessionAndTense(Gender gender,
                                                                                boolean isSingular,
                                                                                Possession possession,
                                                                                Tense tense) {
    return getValues().stream()
                      .filter(o -> o.getSubtense().getTense() == tense)
                      .filter(o -> o.isSingular() == isSingular)
                      .filter(o -> o.getPossession() == possession)
                      .filter(o -> o.getGender() == gender || gender == Gender.X || o.getGender() == Gender.X)
                      .findAny();

  }

  public Conjugation getVerbConjugation(PossessiveWord subject, Subtense tense, Lang lang) {

    if (subject == null) {
      System.err.println("subject is null");
    }
    Optional<Conjugation>
        conjugation =
        this.getConjugationByGenderSingularPossessionAndTense(subject.getGender(lang),
                                                              subject.isSingular(),
                                                              subject.getPossession(),
                                                              tense.getTense());
    if (conjugation.isEmpty()) {
      System.err.println("no conjugation found for");
      return null;
    }
    return conjugation.get();
  }

  // to manage the fact that two files are needed to import a verb (see DB.java)
  public void importConfig(Verb other) {
    this.possibleQuestionIds  = other.getPossibleQuestionIds();
    this.possibleComplements  = other.getPossibleComplements();
    this.verbType             = other.getVerbType();
    this.indirectComplement   = other.isIndirectComplement();
    this.directComplement     = other.isDirectComplement();
    this.dzOppositeComplement = other.isDzOppositeComplement();
    this.objectOnly           = other.isObjectOnly();
    this.dzNoSuffix           = other.isDzNoSuffix();
    this.semiAuxiliar         = other.isSemiAuxiliar();
  }

  public Optional<Conjugation> getImperativeVerbConjugation(final PossessiveWord subject, final Lang lang, final boolean isNegative) {
    Tense tense;
    if (isNegative && lang.getRootLang() == RootLang.AR) {
      tense = Tense.PRESENT; // to manage exception in arabic
    } else {
      tense = Tense.IMPERATIVE;
    }
    return this.getConjugationByGenderSingularPossessionAndTense(subject.getGender(lang),
                                                                 subject.isSingular(),
                                                                 subject.getPossession(),
                                                                 tense);
  }

  public static Set<Verb> deserializeFromList(List<List<String>> entries) {
    int       verbIdIndex    = 0;
    int       tenseIndex     = 1;
    int       pronounIndex   = 2;
    int       frValueIndex   = 3;
    int       dzValueIndex   = 4;
    int       dzValueArIndex = 5;
    int       orderIndex     = 6;
    Set<Verb> verbs          = new HashSet<>();

    for (List<String> values : entries) {
      Verb verb;
      if (values.size() >= 6) {
        Optional<Verb> verbOpt = DB.VERBS.stream().filter(o -> o.getId().equals(values.get(verbIdIndex))).findFirst();
        if (verbOpt.isEmpty()) { // new verb
          verb = new Verb();
          verb.setId(values.get(verbIdIndex));
          verbs.add(verb);
        } else {
          verb = verbOpt.get();
        }

        try {

          Subtense tense     = Subtense.valueOf(values.get(tenseIndex));
          String   pronoun   = values.get(pronounIndex);
          String   frValue   = values.get(frValueIndex);
          String   dzValue   = values.get(dzValueIndex);
          String   dzValueAr = null;
          if (values.size() > dzValueArIndex) {
            dzValueAr = values.get(dzValueArIndex);
          }
          int order = Integer.parseInt(values.get(orderIndex));

          Conjugation conjugation = new Conjugation(List.of(new Translation(Lang.DZ, dzValue, dzValueAr), new Translation(Lang.FR, frValue)),
                                                    Gender.X, true, Possession.I, tense, order);
          verb.getValues().add(conjugation);

        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        System.err.println("adjective ignored because not enough values : " + values.get(0));
      }
    }
    return verbs;
  }
}