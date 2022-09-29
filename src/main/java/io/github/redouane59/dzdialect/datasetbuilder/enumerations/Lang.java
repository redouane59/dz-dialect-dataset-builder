package io.github.redouane59.dzdialect.datasetbuilder.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Lang {
  FR("fr", RootLang.FR, "Français (France)"),
  DZ("dz", RootLang.AR, "Dardja (Alger)");
  // add other dialects here

  final String   id;
  final RootLang rootLang;
  final String   description;
}
