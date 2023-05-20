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

  public Word(String dz, String fr) {
    this.translations = List.of(new Translation(Lang.DZ, dz), new Translation(Lang.FR, fr));
  }

  @Setter
  @Getter
  private List<Translation> translations = new ArrayList<>();

  public Optional<Translation> getTranslation(Lang lang) {
    return translations.stream().filter(t -> t.getLang() == lang).findAny();
  }

  public String getTranslationValue(Lang lang) {
    return getTranslation(lang).orElse(new Translation(lang, "")).getValue();
  }

  public String getTranslationValueAr(Lang lang) {
    return getTranslation(lang).orElse(new Translation(lang, "")).getArValue();
  }

  @Override
  public String toString() {
    return getTranslationValue(Lang.FR) + " -> " + getTranslationValue(Lang.DZ) + " " + getTranslationValueAr(Lang.DZ);
  }
}
