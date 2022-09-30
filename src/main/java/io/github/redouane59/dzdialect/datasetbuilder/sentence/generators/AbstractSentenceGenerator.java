package io.github.redouane59.dzdialect.datasetbuilder.sentence.generators;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence.SentenceContent;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSentenceGenerator {

  public final static Random              RANDOM     = new Random();
  private             GeneratorParameters parameters = GeneratorParameters.builder().build();

  public abstract List<Sentence> generateAllSentences();

  public abstract Map<Lang, List<String>> generateRandomAlternativeWords(SentenceContent content);
}
