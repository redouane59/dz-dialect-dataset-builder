package io.github.redouane59.dzdialect.datasetbuilder.noun;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.WordType;
import io.github.redouane59.dzdialect.datasetbuilder.word.abstracts.AbstractWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;

@Getter
public class Noun extends AbstractWord {

  private final List<GenderedWord> values = new ArrayList<>();
  @JsonInclude(Include.NON_EMPTY)
  private       NounType           type;
/*  @JsonProperty("state_preposition")
  private       String             statePrepositionId;
  @JsonProperty("deplacement_preposition")
  private       String             deplacementPrepositionId;*/

  public Noun(NounType type) {
    setWordType(WordType.NOUN);
    this.type = type;
  }

  public static Set<Noun> deserializeFromList(List<List<String>> entries) {
    int       nounIndex      = 0;
    int       singularIndex  = 1;
    int       frValueIndex   = 2;
    int       frGenderIndex  = 3;
    int       dzValueIndex   = 4;
    int       dzValueArIndex = 5;
    int       dzGenderIndex  = 6;
    int       nounTypeIndex  = 7;
    Set<Noun> nouns          = new HashSet<>();

    for (List<String> values : entries) {
      if (values.size() >= 7) {
        Noun           abstractNoun = new Noun(NounType.valueOf(values.get(nounTypeIndex)));
        Optional<Noun> nounOpt      = nouns.stream().filter(o -> o.getId().equals(values.get(nounIndex))).findFirst();
        if (nounOpt.isEmpty()) { // new noun
          abstractNoun.setId(values.get(nounIndex));
          nouns.add(abstractNoun);
        } else { // existing adjective
          abstractNoun = nounOpt.get();
        }

        try {

          boolean singular  = Boolean.parseBoolean(values.get(singularIndex));
          Gender  frGender  = Gender.valueOf(values.get(frGenderIndex));
          Gender  dzGender  = Gender.valueOf(values.get(dzGenderIndex));
          String  frValue   = values.get(frValueIndex);
          String  dzValue   = values.get(dzValueIndex);
          String  dzValueAr = null;
          if (values.size() > dzValueArIndex) {
            dzValueAr = values.get(dzValueArIndex);
          }

          GenderedWord noun = new GenderedWord();
          noun.setSingular(singular);
          if (frGender == dzGender) {
            noun.setGender(frGender);
            noun.setTranslations(List.of(new Translation(Lang.FR, frValue),
                                         new Translation(Lang.DZ, dzValue, dzValueAr)));
          } else {
            noun.setTranslations(List.of(new Translation(Lang.FR, frValue, frGender, null),
                                         new Translation(Lang.DZ, dzValue, dzGender, dzValueAr)));
          }

          abstractNoun.getValues().add(noun);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        System.err.println("adjective ignored because not enough values : " + values.get(0));
      }
    }

    return nouns;
  }

}