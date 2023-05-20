package io.github.redouane59.dzdialect.datasetbuilder.sentence.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import lombok.Getter;

@Getter
public class WordDTO {

  @JsonProperty("dz")
  private String dz;
  @JsonProperty("dz_ar")
  private String dzAr;
  @JsonProperty("fr")
  private String fr;

  public WordDTO(Word word) {
    if (word != null) {
      this.dz   = word.getTranslationValue(Lang.DZ);
      this.dzAr = word.getTranslationValueAr(Lang.DZ);
      this.fr   = word.getTranslationValue(Lang.FR);
    } else {
      System.err.println("null word");
    }
  }

}
