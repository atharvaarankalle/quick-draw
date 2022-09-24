package nz.ac.auckland.se206.controllers;

public class Score implements Comparable<Score> {
  private String id;
  private String word;
  private int time;

  public Score(String word, int time, String id) {
    this.word = word;
    this.time = time;
    this.id = id;
  }

  public String getID() {
    return id;
  }

  public int getTime() {
    return time;
  }

  public String getWord() {
    return word;
  }

  @Override
  public int compareTo(Score arg0) {
    return this.time - arg0.getTime();
  }
}
