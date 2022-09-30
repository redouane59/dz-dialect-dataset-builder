package io.github.redouane59.dzdialect.datasetbuilder.sentence.generators;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Tense;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeneratorParameters {

  @Builder.Default
  private int            alternativeCount = 3;
  private PossessiveWord pronoun;
  //  private Verb           abstractVerb;
//  private Adjective adjective;
  private Tense          tense;
  private boolean        excludePositive;
  private boolean        excludeNegative;
//  private SentenceSchema  sentenceSchema;

  @Override
  public String toString() {
    return "GeneratorParameters{" +
           "alternativeCount=" + alternativeCount +
           ", pronoun=" + pronoun +
           //     ", adjective=" + adjective +
           //     ", abstractVerb=" + abstractVerb +
           ", tense=" + tense +
           ", excludePositive=" + excludePositive +
           ", excludeNegative=" + excludeNegative +
           '}';
  }
}
