package io.github.redouane59.dzdialect.datasetbuilder;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.OBJECT_MAPPER;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.helper.ResourceList;
import io.github.redouane59.dzdialect.datasetbuilder.noun.Noun;
import io.github.redouane59.dzdialect.datasetbuilder.pronoun.AbstractPronouns;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Verb;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Location;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DB {

  public final static Set<Verb>         VERBS      = new HashSet<>();
  public final static Set<Adjective>    ADJECTIVES = new HashSet<>();
  public final static Set<Noun>         NOUNS      = new HashSet<>();
  public static       Verb              AUX_ETRE;
  public static       Verb              AUX_AVOIR;
  public static       AbstractPronouns  PERSONAL_PRONOUNS;
  public static       Set<Location>     LOCATIONS  = new HashSet<>();
  public static       Set<GenderedWord> PERSONS    = new HashSet<>();

  // to fix
  public static List<PossessiveWord> BROTHER_ENDING = List.of(
      new PossessiveWord("ya", "mon", Gender.X, true, Possession.I),
      new PossessiveWord("ek", "ton", Gender.X, true, Possession.YOU),
      new PossessiveWord("h", "son", Gender.M, true, Possession.OTHER),
      new PossessiveWord("ha", "son", Gender.X, true, Possession.OTHER),
      new PossessiveWord("na", "notre", Gender.X, false, Possession.I),
      new PossessiveWord("koum", "votre", Gender.X, false, Possession.YOU),
      new PossessiveWord("houm", "leur", Gender.X, false, Possession.OTHER)
  );
  public static List<PossessiveWord> SISTER_ENDING  = List.of(
      new PossessiveWord("i", "ma", Gender.X, true, Possession.I),
      new PossessiveWord("ek", "ta", Gender.X, true, Possession.YOU),
      new PossessiveWord("ou", "sa", Gender.M, true, Possession.OTHER),
      new PossessiveWord("ha", "sa", Gender.X, true, Possession.OTHER),
      new PossessiveWord("na", "notre", Gender.X, false, Possession.I),
      new PossessiveWord("koum", "votre", Gender.X, false, Possession.YOU),
      new PossessiveWord("houm", "leur", Gender.X, false, Possession.OTHER)
  );
/*
  public static Word                 BROTHER        = new Word("khou", "frère");
  public static Word                 BROTHERS       = new Word("khawa", "frères");
  public static Word                 SISTER         = new Word("kht", "soeur");
  public static Word                 SISTERS        = new Word("khouatet", "soeurs");
  public static Word                 DAUGTHER       = new Word("bnet", "filles");
  public static Word                 SON            = new Word("wled", "garçons");
  public static Word                 CHILDREN       = new Word("drari", "enfants");
*/

  public static void init() throws IOException {
    initAbstractPronouns();
    initVerbs();
    initAdjectives();
    initNouns();
    initLocations();
    initPersons();
    AUX_ETRE  = DB.VERBS.stream().filter(v -> v.getId().equals("être")).findFirst().get();
    AUX_AVOIR = DB.VERBS.stream().filter(v -> v.getId().equals("avoir")).findFirst().get();
    System.out.println();
  }

  private static void initPersons() {
    String      csvFilePath = "csv/persons.csv";
    InputStream file        = DB.class.getClassLoader().getResourceAsStream(csvFilePath);
    if (file == null) {
      System.err.println("locations.csv file null");
      return;
    }
    try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file, StandardCharsets.UTF_8))
        .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
        .build()) {
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        if (nextLine.length > 3) {
          PERSONS.add(new GenderedWord(nextLine[0], nextLine[1], Gender.valueOf(nextLine[2]), Boolean.parseBoolean(nextLine[3])));
        } else {
          System.err.println("not enough column for location line");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(PERSONS.size() + " persons imported");
  }

  // @todo to finish
  private static void initLocations() {
    String      csvFilePath = "csv/locations.csv";
    InputStream file        = DB.class.getClassLoader().getResourceAsStream(csvFilePath);
    if (file == null) {
      System.err.println("locations.csv file null");
      return;
    }
    try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file, StandardCharsets.UTF_8))
        .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
        .build()) {
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        if (nextLine.length > 3) {
          LOCATIONS.add(new Location(nextLine[0], nextLine[1], nextLine[2], nextLine[3]));
        } else if (nextLine.length > 1) {
          LOCATIONS.add(new Location(nextLine[0], nextLine[1]));
        } else {
          System.err.println("not enough column for location line");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(LOCATIONS.size() + " locations imported");
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
    System.out.println(VERBS.size() + " verbs loaded");
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

  public static void initNouns() {
    NOUNS.clear();
/*    Adjective[] adjectiveConfigurations = new Adjective[]{};
    try {
      adjectiveConfigurations =
          OBJECT_MAPPER.readValue(new File("./src/main/resources/adjectives/.adjective_config.json"), Adjective[].class);
    } catch (Exception e) {
      System.err.println("could not load adjective configurations " + e.getMessage());
      e.printStackTrace();
    }*/
    Set<String> files = new HashSet<>(ResourceList.getResources(Pattern.compile(".*nouns.*json")));
    for (String fileName : files) {
      try {
        if (!fileName.contains("personal_pronouns")) {
          Noun noun = OBJECT_MAPPER.readValue(new File(fileName), Noun.class);
          NOUNS.add(noun);
        }
      } catch (IOException e) {
        System.err.println("could not load noun file " + fileName);
        e.printStackTrace();
      }
    }
    System.out.println(NOUNS.size() + " adjectives loaded");
  }

}
