package io.github.redouane59.dzdialect.datasetbuilder;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.OBJECT_MAPPER;

import io.github.redouane59.dzdialect.datasetbuilder.pronoun.AbstractPronouns;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DB {

  public static AbstractPronouns PERSONAL_PRONOUNS;

  public static void init() throws IOException {
    initAbstractPronouns();
  }

  public static void initAbstractPronouns() throws IOException {
    File file = new File("./src/main/resources/io/github/redouane59/dzdialect/datasetbuilder/other/personal_pronouns.json");
    PERSONAL_PRONOUNS = new AbstractPronouns(List.of(OBJECT_MAPPER.readValue(file, PossessiveWord[].class)));
  }
}
