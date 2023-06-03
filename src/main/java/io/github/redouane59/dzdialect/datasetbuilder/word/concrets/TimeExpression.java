package io.github.redouane59.dzdialect.datasetbuilder.word.concrets;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimeExpression extends Word {

  private Tense tense;

  public TimeExpression(List<Translation> translations, Tense tense) {
    super(translations);
    this.tense = tense;
  }

  public TimeExpression(String dz, String fr, Tense tense) {
    super(dz, fr);
    this.tense = tense;
  }


}
