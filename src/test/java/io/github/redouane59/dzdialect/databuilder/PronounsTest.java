package io.github.redouane59.dzdialect.databuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.dzdialect.datasetbuilder.DB;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Gender;
import io.github.redouane59.dzdialect.datasetbuilder.enumerations.Lang;
import io.github.redouane59.dzdialect.datasetbuilder.word.concrets.Possession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PronounsTest {

  @BeforeAll
  public static void inti() throws IOException {
    DB.init();
  }

  @Test
  public void testDeserializePronouns() {
    assertTrue(DB.PERSONAL_PRONOUNS.getValues().size() > 7);
    assertTrue(DB.PERSONAL_PRONOUNS.getValues().get(0).isSingular());
    assertEquals(Possession.I, DB.PERSONAL_PRONOUNS.getValues().get(0).getPossession());
    assertEquals(Gender.X, DB.PERSONAL_PRONOUNS.getValues().get(0).getGender());
  }

  @Test
  public void testGetPronoun1S() {
    assertEquals("je", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, true, Gender.X).getTranslationValue(Lang.FR));
    assertEquals("je", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, true, Gender.M).getTranslationValue(Lang.FR));
    assertEquals("je", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, true, Gender.F).getTranslationValue(Lang.FR));
    assertEquals("ana", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, true, Gender.X).getTranslationValue(Lang.DZ));
    assertEquals("ana", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, true, Gender.M).getTranslationValue(Lang.DZ));
    assertEquals("ana", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, true, Gender.F).getTranslationValue(Lang.DZ));
  }

  @Test
  public void testGetPronoun2S() {
    assertEquals("tu(m.)", DB.PERSONAL_PRONOUNS.getPronoun(Possession.YOU, true, Gender.M).getTranslationValue(Lang.FR));
    assertEquals("tu(f.)", DB.PERSONAL_PRONOUNS.getPronoun(Possession.YOU, true, Gender.F).getTranslationValue(Lang.FR));
    assertEquals("enta", DB.PERSONAL_PRONOUNS.getPronoun(Possession.YOU, true, Gender.M).getTranslationValue(Lang.DZ));
    assertEquals("enti", DB.PERSONAL_PRONOUNS.getPronoun(Possession.YOU, true, Gender.F).getTranslationValue(Lang.DZ));
  }

  @Test
  public void testGetPronoun3S() {
    assertEquals("il", DB.PERSONAL_PRONOUNS.getPronoun(Possession.OTHER, true, Gender.M).getTranslationValue(Lang.FR));
    assertEquals("elle", DB.PERSONAL_PRONOUNS.getPronoun(Possession.OTHER, true, Gender.F).getTranslationValue(Lang.FR));
    assertEquals("houwa", DB.PERSONAL_PRONOUNS.getPronoun(Possession.OTHER, true, Gender.M).getTranslationValue(Lang.DZ));
    assertEquals("hiya", DB.PERSONAL_PRONOUNS.getPronoun(Possession.OTHER, true, Gender.F).getTranslationValue(Lang.DZ));
  }

  @Test
  public void testGetPronoun1P() {
    assertEquals("on", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, false, Gender.X).getTranslationValue(Lang.FR));
    assertEquals("7na", DB.PERSONAL_PRONOUNS.getPronoun(Possession.I, false, Gender.X).getTranslationValue(Lang.DZ));
  }

  @Test
  public void testGetPronoun2P() {
    assertEquals("vous", DB.PERSONAL_PRONOUNS.getPronoun(Possession.YOU, false, Gender.X).getTranslationValue(Lang.FR));
    assertEquals("ntouma", DB.PERSONAL_PRONOUNS.getPronoun(Possession.YOU, false, Gender.X).getTranslationValue(Lang.DZ));
  }

  @Test
  public void testGetPronoun3P() {
    assertEquals("ils/elles", DB.PERSONAL_PRONOUNS.getPronoun(Possession.OTHER, false, Gender.X).getTranslationValue(Lang.FR));
    assertEquals("houma", DB.PERSONAL_PRONOUNS.getPronoun(Possession.OTHER, false, Gender.X).getTranslationValue(Lang.DZ));
  }

  @Test
  public void printPronouns() {
    DB.PERSONAL_PRONOUNS.getValues().forEach(System.out::println);
  }
}
