package io.github.redouane59.dzdialect.datasetbuilder.word;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.dzdialect.datasetbuilder.Config;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Subtense;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Translation;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConjugationListDeserializer extends JsonDeserializer {

  @Override
  public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
  throws IOException {
    JsonNode           arrNode = jsonParser.getCodec().readTree(jsonParser);
    List<? super Word> result  = new ArrayList<>();
    if (arrNode.isArray()) {
      for (final JsonNode node : arrNode) {
        List<Translation> translations = Config.OBJECT_MAPPER.readValue(node.get("translations").toString(), new TypeReference<List<Translation>>() {
        });
        if (node.has("singular") && (node.has("gender") || translations.stream().findFirst().get().getGender() != null)) {
          Gender gender = null;
          if (node.has("gender")) {
            gender = Gender.valueOf(node.get("gender").asText());
          }
          boolean singular = node.get("singular").asBoolean();
          if (node.has("possession")) {
            Possession possession = Possession.valueOf(node.get("possession").asText());
            int        index      = 0;
            if (node.has("index")) {
              index = node.get("index").asInt();
            }
            if (node.has("tense")) {
              Subtense tense = Subtense.valueOf(node.get("tense").asText());

              result.add(new Conjugation(translations, gender, singular, possession, tense, index));
            } else {
              result.add(new PossessiveWord(translations, gender, singular, possession, index));
            }
          } else {
            result.add(new GenderedWord(translations, gender, singular));
          }
        } else {
          result.add(new Word(translations));
        }
      }
    }

    return result;
  }
}
