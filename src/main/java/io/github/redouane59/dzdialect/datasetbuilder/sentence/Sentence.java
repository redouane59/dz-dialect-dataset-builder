package io.github.redouane59.dzdialect.datasetbuilder.sentence;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.RootLang;
import io.github.redouane59.dzdialect.datasetbuilder.noun.Noun;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.util.Arrays;
import java.util.Collections;
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

  private SentenceContent         content;
  @JsonInclude(Include.NON_EMPTY)
  private Map<Lang, List<String>> randomWords = new HashMap<>();
  private SentenceSchema          sentenceSchema;

  public Sentence(List<Translation> translations) {
    super(translations);
  }

  public Map<Lang, List<String>> getRandomWords() {
    randomWords.forEach((key, value) -> Collections.shuffle(value));
    return randomWords;
  }

  public List<String> getSplittedWords(Lang lang) {
    String sentence = getTranslationValue(lang);
    if (lang.getRootLang() == RootLang.FR) {
      sentence = sentence.replace("'", "' ");
    }
    return Arrays.asList(sentence.split(" "));
  }

  @Data
  @Builder
  public static class SentenceContent {

    private List<PossessiveWord> pronouns;
    private List<Noun>           nouns;
    private List<Adjective>      adjectives;
    private List<Verb>           verbs;
    private String               subtense;
    private boolean              negation;

  }

}
