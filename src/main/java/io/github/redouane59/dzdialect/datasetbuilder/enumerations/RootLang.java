package io.github.redouane59.dzdialect.datasetbuilder.enumerations;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RootLang {
  FR("français"),
  AR("arabe");

  final String value;
}