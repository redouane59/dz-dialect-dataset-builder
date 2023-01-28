package io.github.redouane59.dzdialect.datasetbuilder.word.concrets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GenderedWord extends Word {

  @JsonInclude(Include.NON_NULL)
  private Gender  gender;
  private boolean singular;

  public GenderedWord(List<Translation> translations, Gender gender, boolean singular) {
    super(translations);
    this.gender   = gender;
    this.singular = singular;
  }

  public Gender getGender(Lang lang) {
    if (this.getTranslation(lang).isEmpty()) {
      return gender;
    }
    return this.getTranslation(lang).get().getGender() != null
           ? this.getTranslation(lang).get().getGender()
           : gender;
  }

  @Override
  public String toString() {
    String result = super.toString();
    result += " (" + this.gender + "/";
    if (this.isSingular()) {
      result += "sg";
    } else {
      result += "pl";
    }
    return result + ")";
  }

}