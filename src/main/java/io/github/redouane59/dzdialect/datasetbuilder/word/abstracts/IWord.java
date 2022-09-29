package io.github.redouane59.dzdialect.datasetbuilder.word.abstracts;

import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Word;
import java.util.List;

public interface IWord {

  /**
   * Get all the possible variations of the word
   */
  List<? extends Word> getValues();
}
