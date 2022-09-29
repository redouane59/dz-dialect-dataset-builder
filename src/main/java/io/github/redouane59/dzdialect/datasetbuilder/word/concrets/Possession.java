package io.github.redouane59.dzdialect.datasetbuilder.word.concrets;

import static io.github.redouane59.dzdialect.datasetbuilder.Config.RANDOM;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Possession {

  I, // related to I, me, mine, our, ours, etc.
  YOU, // related to you, your, yours, etc.
  OTHER; // related to he, she, his, her, their, etc.

  public static Possession getRandomPosession() {
    return Arrays.stream(values()).skip(RANDOM.nextInt(values().length)).findFirst().get();
  }

  public static Possession getRandomPosession(Possession otherPossession, boolean objectOnly, boolean isImperative) {
    Set<Possession> matchingPossession = Set.of(values());
    if (isImperative) {
      matchingPossession = Set.of(Possession.I, Possession.OTHER);
    } else if (!objectOnly) {
      matchingPossession =
          matchingPossession.stream().filter(o -> o != otherPossession || otherPossession == Possession.OTHER).collect(Collectors.toSet());
    } else {
      matchingPossession = matchingPossession.stream().filter(o -> o == Possession.OTHER).collect(Collectors.toSet());
    }
    return matchingPossession.stream().skip(RANDOM.nextInt(matchingPossession.size())).findFirst().get();
  }

}