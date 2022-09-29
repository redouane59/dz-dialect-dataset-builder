package io.github.redouane59.dzdialect.datasetbuilder.pronoun;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.WordType;
import io.github.redouane59.dzdialect.datasetbuilder.word.ConjugationListDeserializer;
import io.github.redouane59.dzdialect.datasetbuilder.word.abstracts.AbstractWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class AbstractPronouns extends AbstractWord {

  @JsonInclude(Include.NON_EMPTY)
  @JsonDeserialize(using = ConjugationListDeserializer.class)
  private List<PossessiveWord> values; // only one value / pronoun

  public AbstractPronouns(List<PossessiveWord> values) {
    this.setWordType(WordType.PRONOUN);
    this.values = values;
  }

  public PossessiveWord getPronoun(Possession possession, boolean isSingular, Gender gender) {
    Optional<PossessiveWord> result = getValues().stream()
                                                 .filter(o -> o.getPossession() == possession)
                                                 .filter(o -> o.getGender() == gender
                                                              || o.getGender() == Gender.X
                                                              || gender == Gender.X)
                                                 .filter(o -> o.isSingular() == isSingular)
                                                 .findAny();
    if (result.isEmpty()) {
      System.err.println("no pronoun found");
      return null;
    }
    return result.get();
  }


}
