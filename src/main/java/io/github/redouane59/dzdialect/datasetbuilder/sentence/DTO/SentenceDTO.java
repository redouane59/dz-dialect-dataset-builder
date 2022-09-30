package io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence;
import io.github.redouane59.dzdialect.datasetbuilder.sentence.Sentence.SentenceContent;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_DEFAULT)
public class SentenceDTO extends WordDTO {

  @JsonProperty("word_propositions")
  @JsonInclude(value = Include.CUSTOM, valueFilter = WordPropositionsDTO.class)
  private WordPropositionsDTO wordPropositions;
  @JsonProperty("additionnal_information")
  private SentenceContentDTO  sentenceContent;

  public SentenceDTO(Sentence sentence) {
    super(sentence);
    if (sentence.getContent() != null) {
      this.sentenceContent = new SentenceContentDTO(sentence.getContent());
    }
    wordPropositions = new WordPropositionsDTO(sentence.getRandomWords());
  }

  @NoArgsConstructor
  @Getter
  public static class WordPropositionsDTO {

    @JsonProperty("dz")
    @JsonInclude(Include.NON_EMPTY)
    private List<String> dz   = new ArrayList<>();
    @JsonProperty("dz_ar")
    @JsonInclude(Include.NON_EMPTY)
    private List<String> dzAr = new ArrayList<>();
    @JsonProperty("fr")
    @JsonInclude(Include.NON_EMPTY)
    private List<String> fr   = new ArrayList<>();

    public WordPropositionsDTO(final Map<Lang, List<String>> randomWords) {
      this.dz   = randomWords.get(Lang.DZ);
      this.dzAr = randomWords.get(Lang.DZ + "_ar");
      this.fr   = randomWords.get(Lang.FR);
    }

    // to manage empty serialization
    @Override
    public boolean equals(Object other) {
      if (!(other instanceof WordPropositionsDTO)) {
        return false;
      }
      WordPropositionsDTO prop2 = (WordPropositionsDTO) other;
      return (this.dz.equals(prop2.getDz()) && this.fr.equals(prop2.getFr())
              || (this.dz.size() == 0 && prop2.getDz() == null && this.fr.size() == 0 && prop2.getFr() == null));
    }
  }

  @NoArgsConstructor
  public static class SentenceContentDTO {

    @JsonProperty("verbs")
    @JsonInclude(Include.NON_NULL)
    private List<String> abstractVerbs;
    @JsonProperty("adverbs")
    @JsonInclude(Include.NON_NULL)
    private List<String> abstractAdverbs;
    @JsonProperty("questions")
    @JsonInclude(Include.NON_NULL)
    private List<String> abstractQuestions;
    @JsonProperty("adjectives")
    @JsonInclude(Include.NON_NULL)
    private List<String> abstractAdjectives;
    @JsonProperty("nouns")
    @JsonInclude(Include.NON_NULL)
    private List<String> abstractNouns;
    @JsonProperty("pronouns")
    @JsonInclude(Include.NON_NULL)
    private List<String> abstractPronouns;
    @JsonProperty("tense")
    @JsonInclude(Include.NON_NULL)
    private String       subtense;
    @JsonProperty("schema")
    @JsonInclude(Include.NON_NULL)
    private String       sentenceSchema;
    @JsonInclude(Include.NON_DEFAULT)
    private boolean      negation;

    public SentenceContentDTO(final SentenceContent content) {
      if (content.getPronouns().size() > 0) {
        this.abstractPronouns = content.getPronouns().stream().map(PossessiveWord::getId).collect(Collectors.toList());
      }
      this.negation = content != null && content.isNegation();
    }
  }
}
