package io.github.redouane59.dzdialect.datasetbuilder.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Subtense {
  PAST_AVOIR(Tense.PAST),
  PAST_ETRE(Tense.PAST),
  PAST_IMPARFAIT(Tense.PAST),
  PRESENT(Tense.PRESENT),
  FUTURE(Tense.FUTURE),
  IMPERATIVE(Tense.IMPERATIVE);

  Tense tense;
}