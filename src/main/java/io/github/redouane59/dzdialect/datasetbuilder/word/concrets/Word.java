package io.github.redouane59.dzdialect.datasetbuilder.word.concrets;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Word {

  @Setter
  @Getter
  private List<Translation> translations = new ArrayList<>();

  public Optional<Translation> getTranslationByLang(Lang lang) {
    return translations.stream().filter(t -> t.getLang() == lang).findAny();
  }

  public String getTranslationValue(Lang lang) {
    return getTranslationByLang(lang).orElse(new Translation(lang, "")).getValue();
  }

  public String getTranslationValueAr(Lang lang) {
    return getTranslationByLang(lang).orElse(new Translation(lang, "")).getArValue();
  }


}
