package io.github.redouane59.dzdialect.datasetbuilder.verb;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Subtense;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Conjugation extends PossessiveWord {

  private Subtense subtense;

  public Conjugation(List<Translation> translations, Gender gender, boolean singular, Possession possession, Subtense subtense, int index) {
    super(translations, gender, singular, possession, index);
    this.subtense = subtense;
  }


}