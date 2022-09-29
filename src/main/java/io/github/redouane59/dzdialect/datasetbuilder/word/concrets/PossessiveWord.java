package io.github.redouane59.dzdialect.datasetbuilder.word.concrets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PossessiveWord extends GenderedWord implements Comparable {

  private Possession possession;
  @JsonInclude(Include.NON_DEFAULT)
  @Setter
  @Getter
  private int        index;

  public PossessiveWord(List<Translation> translation, Gender gender, boolean singular, Possession possession, int index) {
    super(translation, gender, singular);
    this.possession = possession;
    this.index      = index;
  }

  @Override
  public int compareTo(final Object o) {
    return (getIndex() - ((Conjugation) o).getIndex());
  }

  @Override
  public String toString() {
    return getTranslationValue(Lang.FR) + " -> " + getTranslationValue(Lang.DZ) + " " + getTranslationValueAr(Lang.DZ);
  }

  public String getId() {
    return getPossession() + "-" + getGender() + "-" + (isSingular() ? "S" : "P");
  }

}
