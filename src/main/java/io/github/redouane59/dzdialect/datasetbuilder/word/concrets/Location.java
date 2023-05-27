package io.github.redouane59.dzdialect.datasetbuilder.word.concrets;

import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Location {

  private Word at;
  private Word to;

  public Location(String dzAt, String frAt) {
    this.at = new Word(List.of(new Translation(Lang.DZ, dzAt), new Translation(Lang.FR, frAt)));
  }

  public Location(String dzAt, String frAt, String dzTo, String frTo) {
    this(dzAt, frAt);
    this.to = new Word(List.of(new Translation(Lang.DZ, dzTo), new Translation(Lang.FR, frTo)));
  }

}
