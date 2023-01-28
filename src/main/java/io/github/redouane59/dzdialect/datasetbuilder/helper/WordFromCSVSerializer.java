package io.github.redouane59.dzdialect.datasetbuilder.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.redouane59.dzdialect.datasetbuilder.verb.Conjugation;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.GenderedWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.PossessiveWord;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.io.IOException;

public class WordFromCSVSerializer extends StdSerializer<Word> {

  public WordFromCSVSerializer() {
    this(null);
  }

  public WordFromCSVSerializer(final Class<Word> t) {
    super(t);
  }

  @Override
  public void serialize(final Word word, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeObjectField("translations", word.getTranslations());
    if (word instanceof GenderedWord) {
      jsonGenerator.writeObjectField("gender", ((GenderedWord) word).getGender());
    }
    if (word instanceof PossessiveWord) {
      if (((PossessiveWord) word).getPossession() != null) {
        jsonGenerator.writeObjectField("possession", ((PossessiveWord) word).getPossession());
      }
      jsonGenerator.writeObjectField("singular", ((PossessiveWord) word).isSingular());
    }
    if (word instanceof Conjugation) {
      jsonGenerator.writeObjectField("tense", ((Conjugation) word).getSubtense());
    }

    jsonGenerator.writeEndObject();
  }
}
