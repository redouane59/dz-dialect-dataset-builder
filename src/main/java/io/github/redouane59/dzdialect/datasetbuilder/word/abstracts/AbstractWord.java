package io.github.redouane59.dzdialect.datasetbuilder.word.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.WordType;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public abstract class AbstractWord implements IWord {

  private String   id;
  @JsonProperty("word_type")
  @JsonIgnore
  private WordType wordType;

  @JsonIgnore
  public Optional<GenderedWord> getWordByGenderAndSingular(Gender gender, Lang lang, boolean isSingular) {
    Optional<GenderedWord> result = getValues().stream()
                                               .map(o -> (GenderedWord) o)
                                               .filter(o -> o.getGender(lang) == gender
                                                            || o.getGender(lang) == Gender.X
                                                            || gender == Gender.X)

                                               .filter(o -> o.isSingular() == isSingular)
                                               .findAny();
    if (result.isEmpty()) {
      System.out.println("no gendered word found");
      return Optional.empty();
    }
    return result;
  }


  @Override
  public String toString() {
    return "AbstractWord{" +
           "id='" + id + '\'' +
           ", wordType=" + wordType +
           '}';
  }
}
