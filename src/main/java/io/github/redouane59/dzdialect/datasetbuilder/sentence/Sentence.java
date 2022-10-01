package io.github.redouane59.dzdialect.datasetbuilder.sentence;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sentence extends Word {

  @JsonProperty("additional_information")
  private SentenceContent         content;
  @JsonProperty("word_propositions")
  @JsonInclude(Include.NON_EMPTY)
  private Map<Lang, List<String>> randomWords = new HashMap<>();

  public Sentence(List<Translation> translations) {
    super(translations);
  }

  @Data
  @Builder
  public static class SentenceContent {

    private List<PossessiveWord> pronouns;
    private List<Adjective>      adjectives;
    private String               subtense;
    private boolean              negation;

  }

}
