package io.github.redouane59.dzdialect.databuilder.in;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.redouane59.dzdialect.datasetbuilder.Config;
import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.adjective.Adjective;
import io.github.redouane59.dzdialect.datasetbuilder.gsheets.SheetsHelper;
import io.github.redouane59.dzdialect.datasetbuilder.helper.WordFromCSVSerializer;
import io.github.redouane59.dzdialect.datasetbuilder.noun.Noun;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DataIntegrationTest {

/*  @Test
  public void parseAdjectiveTest() {

    String         fileName   = "adjectives.csv";
    Set<Adjective> adjectives = Adjective.deserializeFromCSV(fileName, true);

    adjectives.forEach(o -> {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addSerializer(Word.class, new WordFromCSVSerializer());
      mapper.registerModule(module);
      try {
        System.out.println(Config.OBJECT_MAPPER.writeValueAsString(o));
        mapper.writeValue(Paths.get("./src/test/resources/imported_adjectives/" + o.getId() + ".json").toFile(), o);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }*/

  @Test
  public void importAdjectivesFromSheets() throws GeneralSecurityException, IOException {
    List<List<String>> result = SheetsHelper.readLine("adjectives!A2:F");
    assertNotNull(result);
    Set<Adjective> adjectives = Adjective.deserializeFromList(result);

    adjectives.forEach(o -> {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addSerializer(Word.class, new WordFromCSVSerializer());
      mapper.registerModule(module);
      try {
        System.out.println(Config.OBJECT_MAPPER.writeValueAsString(o));
        mapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get("./src/test/resources/imported_adjectives/" + o.getId() + ".json").toFile(), o);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @Test
  public void testAdjectiveConfig() {
    for (Adjective adjective : DB.ADJECTIVES) {
      if (adjective.getPossibleNouns().isEmpty()) {
        System.err.println("no configuration found for adjective " + adjective.getId());
      }
    }
  }

  @Test
  public void importNounsFromSheets() throws GeneralSecurityException, IOException {
    List<List<String>> result = SheetsHelper.readLine("nouns!A2:H");
    assertNotNull(result);
    Set<Noun> nouns = Noun.deserializeFromList(result);

    nouns.forEach(o -> {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addSerializer(Word.class, new WordFromCSVSerializer());
      mapper.registerModule(module);
      try {
        System.out.println(Config.OBJECT_MAPPER.writeValueAsString(o));
        mapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get("./src/test/resources/imported_nouns/" + o.getId() + ".json").toFile(), o);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

}
