package io.github.redouane59.dzdialect.datasetbuilder;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.OBJECT_MAPPER;

import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.helper.ResourceList;
import io.github.redouane59.dzdialect.datasetbuilder.pronoun.AbstractPronouns;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DB {

  public final static Set<Verb>        VERBS      = new HashSet<>();
  public final static Set<Adjective>   ADJECTIVES = new HashSet<>();
  public static       Verb             AUX_ETRE;
  public static       Verb             AUX_AVOIR;
  public static       AbstractPronouns PERSONAL_PRONOUNS;

  public static void init() throws IOException {
    initAbstractPronouns();
    initVerbs();
    initAdjectives();
    AUX_ETRE  = DB.VERBS.stream().filter(v -> v.getId().equals("être")).findFirst().get();
    AUX_AVOIR = DB.VERBS.stream().filter(v -> v.getId().equals("avoir")).findFirst().get();
  }

  public static void initAbstractPronouns() throws IOException {
    File file = new File("./src/main/resources/io/github/redouane59/dzdialect/datasetbuilder/other/personal_pronouns.json");
    PERSONAL_PRONOUNS = new AbstractPronouns(List.of(OBJECT_MAPPER.readValue(file, PossessiveWord[].class)));
  }

  public static void initVerbs() {
    VERBS.clear();
    Verb[] verbConfigurations = new Verb[]{};
    try {
      verbConfigurations =
          OBJECT_MAPPER.readValue(new File("./src/main/resources/verbs/.verb_config.json"), Verb[].class);
    } catch (Exception e) {
      System.err.println("could not load verb configurations " + e.getMessage());
      e.printStackTrace();
    }
    Set<String> files = new HashSet<>(ResourceList.getResources(Pattern.compile(".*verbs.*json")))
        .stream().filter(o -> !o.contains(".verb_config.json")).collect(Collectors.toSet());
    for (String fileName : files) {
      Verb verb;
      try {
        verb = OBJECT_MAPPER.readValue(new File(fileName), Verb.class);
        Optional<Verb> verbConfiguration = Arrays.stream(verbConfigurations).filter(o -> o.getId().equals(verb.getId())).findFirst();
        if (verbConfiguration.isPresent()) {
          verb.importConfig(verbConfiguration.get());
        } else {
          System.out.println("no configuration found for verb " + verb.getId());
        }
        VERBS.add(verb);
      } catch (IOException e) {
        System.err.println("could not load verb  " + e.getMessage());
        e.printStackTrace();
      }
    }
    System.out.println(VERBS + " verbs loaded");
  }

  public static void initAdjectives() {
    ADJECTIVES.clear();
    Adjective[] adjectiveConfigurations = new Adjective[]{};
    try {
      adjectiveConfigurations =
          OBJECT_MAPPER.readValue(new File("./src/main/resources/adjectives/.adjective_config.json"), Adjective[].class);
    } catch (Exception e) {
      System.err.println("could not load adjective configurations " + e.getMessage());
      e.printStackTrace();
    }
    Set<String>
        files =
        new HashSet<>(ResourceList.getResources(Pattern.compile(".*adjectives.*json")))
            .stream().filter(o -> !o.contains(".adjective_config.json")).collect(Collectors.toSet());
    for (String fileName : files) {
      try {
        Adjective adjective = OBJECT_MAPPER.readValue(new File(fileName), Adjective.class);
        Optional<Adjective>
            adjectiveConfiguration =
            Arrays.stream(adjectiveConfigurations).filter(o -> o.getId().equals(adjective.getId())).findFirst();
        if (adjectiveConfiguration.isPresent()) {
          adjective.importConfig(adjectiveConfiguration.get());
        } else {
          System.out.println("no configuration found for adjective " + adjective.getId());
        }
        ADJECTIVES.add(adjective);
      } catch (IOException e) {
        System.err.println("could not load adjective file " + fileName);
        e.printStackTrace();
      }
    }
    System.out.println(ADJECTIVES.size() + " adjectives loaded");
  }


}
