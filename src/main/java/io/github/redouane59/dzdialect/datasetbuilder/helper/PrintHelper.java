package io.github.redouane59.dzdialect.datasetbuilder.helper;

public class PrintHelper {

  public static void printSentence(String dz, String fr) {
    dz = dz.replace("khtn", "khotn")
           .replace("khth", "khoth")
           .replace("khtk", "khotk");
    System.out.println(dz + "," + fr);
  }
}
